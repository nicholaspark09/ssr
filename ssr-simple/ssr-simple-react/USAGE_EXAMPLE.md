# Usage Example: Converting TimesheetSubmissionsDashboard

This example shows how to convert your existing `TimesheetSubmissionsDashboard` component to use SSR-Simple React.

## Original Component Structure

Your current dashboard renders a table with:
- Employee names
- Latest timesheet dates
- Status chips
- Action buttons

## Using SSR-Simple React

Instead of hardcoding the UI in React components, you can generate the JSON structure dynamically based on your data.

### Step 1: Create a Function to Generate Table JSON

```typescript
import { NodeModel, TableColumnModel, TableCellModel } from 'ssr-simple-react';

interface EmployeeTimesheetInfo {
  employee: Employee;
  contract: Contract | null;
  latestTimesheet: TimesheetModel | null;
}

function createTimesheetTableJSON(
  employeeTimesheets: EmployeeTimesheetInfo[],
  previousWeekRange: { start: string; end: string },
  currentWeekRange: { start: string; end: string }
): NodeModel {
  const columns: TableColumnModel[] = [
    {
      header: 'Employee',
      weight: 2,
      horizontalAlignment: 'start',
      headerStyle: {
        fontWeight: 'bold',
        fontSize: 14
      }
    },
    {
      header: 'Latest Timesheet',
      weight: 2,
      horizontalAlignment: 'start',
      headerStyle: {
        fontWeight: 'bold',
        fontSize: 14
      }
    },
    {
      header: 'Status',
      weight: 1.5,
      horizontalAlignment: 'center',
      headerStyle: {
        fontWeight: 'bold',
        fontSize: 14
      }
    },
    {
      header: 'Action',
      weight: 1,
      horizontalAlignment: 'center',
      headerStyle: {
        fontWeight: 'bold',
        fontSize: 14
      }
    }
  ];

  const tableData: TableCellModel[][] = employeeTimesheets.map((info) => {
    const status = getTimesheetStatus(info, previousWeekRange, currentWeekRange);

    return [
      {
        text: `${info.employee.first_name} ${info.employee.last_name}`
      },
      {
        text: info.latestTimesheet
          ? formatDateRange(info.latestTimesheet.range_key)
          : 'No timesheets'
      },
      {
        text: status.label,
        backgroundColor: getStatusColor(status.color),
        textStyle: {
          color: '#FFFFFF',
          fontWeight: 'bold',
          fontSize: 12
        }
      },
      {
        text: info.latestTimesheet ? 'View' : '',
        action: info.latestTimesheet
          ? `view_timesheet_${info.employee.range_key}`
          : undefined,
        backgroundColor: info.latestTimesheet ? '#1976D2' : undefined,
        textStyle: info.latestTimesheet ? {
          color: '#FFFFFF',
          fontWeight: 'bold',
          fontSize: 12
        } : undefined
      }
    ];
  });

  return {
    type: 'Card',
    elevation: 2,
    children: [
      {
        type: 'Column',
        modifier: { padding: 16 },
        children: [
          {
            type: 'Row',
            modifier: {
              paddingBottom: 16,
              verticalAlignment: 'center'
            },
            children: [
              {
                type: 'Text',
                title: 'Timesheet Submissions',
                textStyle: {
                  fontSize: 18,
                  fontWeight: 'bold'
                },
                modifier: { weight: 1 }
              },
              {
                type: 'Button',
                label: 'Refresh',
                action: 'refresh_data',
                buttonVariant: 'text'
              }
            ]
          },
          {
            type: 'Table',
            showBorders: true,
            headerBackgroundColor: '#1976D2',
            columns,
            tableData,
            modifier: {
              fillMaxWidth: true
            }
          }
        ]
      }
    ]
  };
}

function getStatusColor(color: string): string {
  const colorMap: Record<string, string> = {
    success: '#4CAF50',
    warning: '#FF9800',
    error: '#F44336',
    info: '#2196F3',
    default: '#9E9E9E'
  };
  return colorMap[color] || colorMap.default;
}

function getTimesheetStatus(
  info: EmployeeTimesheetInfo,
  previousWeekRange: { start: string; end: string },
  currentWeekRange: { start: string; end: string }
) {
  if (!info.contract) {
    return { label: 'No Contract', color: 'default' };
  }

  if (!info.latestTimesheet) {
    return { label: 'No Timesheets', color: 'error' };
  }

  const previousWeekDateString = `${previousWeekRange.start}_${previousWeekRange.end}`;
  const currentWeekDateString = `${currentWeekRange.start}_${currentWeekRange.end}`;

  const isRecentTimesheet =
    info.latestTimesheet.range_key === currentWeekDateString ||
    info.latestTimesheet.range_key === previousWeekDateString;

  if (isRecentTimesheet) {
    if (info.latestTimesheet.status === 'APPROVED') {
      return { label: 'Approved', color: 'success' };
    } else if (info.latestTimesheet.status === 'SUBMITTED') {
      return { label: 'Needs Review', color: 'warning' };
    } else {
      return { label: info.latestTimesheet.status, color: 'info' };
    }
  } else {
    const timesheetEndDate = info.latestTimesheet.range_key.split('_')[1];
    return {
      label: `Last: ${formatDate(timesheetEndDate)}`,
      color: 'warning'
    };
  }
}

function formatDate(dateString: string): string {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
}

function formatDateRange(rangeKey: string): string {
  if (!rangeKey) return 'N/A';
  const [start, end] = rangeKey.split('_');
  return `${formatDate(start)} - ${formatDate(end)}`;
}
```

### Step 2: Use SSRRenderer in Your Component

```typescript
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SSRRenderer } from 'ssr-simple-react';
import { CircularProgress, Box } from '@mui/material';

const TimesheetSubmissionsDashboardComponent: React.FC = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(true);
  const [employeeTimesheets, setEmployeeTimesheets] = useState<EmployeeTimesheetInfo[]>([]);
  const [previousWeekRange, setPreviousWeekRange] = useState<{ start: string; end: string }>({ start: '', end: '' });
  const [currentWeekRange, setCurrentWeekRange] = useState<{ start: string; end: string }>({ start: '', end: '' });

  useEffect(() => {
    const prevWeekRange = TimesheetRepository.getPreviousWeekDateRange();
    setPreviousWeekRange(prevWeekRange);

    const currWeekRange = TimesheetRepository.getCurrentWeekDateRange();
    setCurrentWeekRange(currWeekRange);

    fetchTimesheetData();
  }, []);

  const fetchTimesheetData = async (forceRefresh = false) => {
    setLoading(true);
    // ... your existing data fetching logic ...
    setEmployeeTimesheets(combinedData);
    setLoading(false);
  };

  const handleAction = (action: string, data?: any) => {
    if (action === 'refresh_data') {
      fetchTimesheetData(true);
    } else if (action.startsWith('view_timesheet_')) {
      const employeeId = action.replace('view_timesheet_', '');
      const info = employeeTimesheets.find(e => e.employee.range_key === employeeId);
      if (info?.latestTimesheet) {
        navigate(`/timesheetsadmin/review?partitionKey=${info.latestTimesheet.partition_key}&rangeKey=${info.latestTimesheet.range_key}`, {
          state: {
            timesheet: info.latestTimesheet,
            contract: info.contract,
            teamMember: info.employee
          }
        });
      }
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 2 }}>
        <CircularProgress size={24} />
      </Box>
    );
  }

  const tableConfig = createTimesheetTableJSON(
    employeeTimesheets,
    previousWeekRange,
    currentWeekRange
  );

  return <SSRRenderer nodeModel={tableConfig} onAction={handleAction} />;
};

export default TimesheetSubmissionsDashboardComponent;
```

## Benefits of This Approach

1. **Separation of Concerns**: Data fetching logic is separate from UI rendering
2. **Reusability**: The same JSON structure can be used for Android and Web
3. **Server-Side Generation**: You could generate this JSON from your backend API
4. **Easier Testing**: Test data transformation separately from UI rendering
5. **Dynamic UIs**: Change table structure without modifying component code

## Advanced: Fetch JSON from Backend

You could even fetch the entire table configuration from your backend:

```typescript
const TimesheetSubmissionsDashboardComponent: React.FC = () => {
  const [tableJSON, setTableJSON] = useState<string>('');

  useEffect(() => {
    fetch('/api/timesheets/dashboard-config')
      .then(res => res.text())
      .then(json => setTableJSON(json));
  }, []);

  const handleAction = (action: string, data?: any) => {
    // Handle actions...
  };

  return <SSRRenderer jsonString={tableJSON} onAction={handleAction} />;
};
```

This allows you to change the dashboard layout and styling from the backend without redeploying your frontend!
