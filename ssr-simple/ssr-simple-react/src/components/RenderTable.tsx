import React from 'react';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import { NodeModel, TableCellModel, TableColumnModel } from '../models';
import { modifierToSx, textStyleToCss } from '../utils';

export interface RenderTableProps {
  node: NodeModel;
  onAction?: (action: string, rowData?: any) => void;
}

export const RenderTable: React.FC<RenderTableProps> = ({ node, onAction = () => {} }) => {
  const { columns = [], tableData = [], showBorders, headerBackgroundColor, rowAction, roundedCorners } = node;

  const getAlignment = (align?: string): 'left' | 'center' | 'right' | 'inherit' => {
    if (align === 'start' || align === 'left') return 'left';
    if (align === 'end' || align === 'right') return 'right';
    if (align === 'center') return 'center';
    return 'inherit';
  };

  const getColumnWidth = (column: TableColumnModel): string | number => {
    if (column.width !== undefined) return `${column.width}px`;
    if (column.weight !== undefined) {
      const totalWeight = columns.reduce((sum, col) => sum + (col.weight || 1), 0);
      return `${(column.weight / totalWeight) * 100}%`;
    }
    return 'auto';
  };

  return (
    <TableContainer
      component={Paper}
      variant={showBorders ? 'outlined' : 'elevation'}
      sx={{
        ...modifierToSx(node.modifier),
        borderRadius: roundedCorners ? `${roundedCorners}px` : undefined,
      }}
    >
      <Table size="small">
        <TableHead>
          <TableRow>
            {columns.map((column, colIndex) => (
              <TableCell
                key={colIndex}
                align={getAlignment(column.horizontalAlignment)}
                sx={{
                  backgroundColor: headerBackgroundColor,
                  width: getColumnWidth(column),
                  borderRight: showBorders && colIndex < columns.length - 1 ? '1px solid rgba(224, 224, 224, 1)' : undefined,
                  ...textStyleToCss(column.headerStyle),
                }}
              >
                {column.header}
              </TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          {tableData.map((row, rowIndex) => (
            <TableRow
              key={rowIndex}
              hover={!!rowAction}
              onClick={() => rowAction && onAction(rowAction, row)}
              sx={{
                cursor: rowAction ? 'pointer' : 'default',
              }}
            >
              {row.map((cell, colIndex) => {
                const column = columns[colIndex];
                const cellTextStyle = cell.textStyle || column?.textStyle;

                return (
                  <TableCell
                    key={colIndex}
                    align={getAlignment(column?.horizontalAlignment)}
                    onClick={(e) => {
                      if (cell.action) {
                        e.stopPropagation();
                        onAction(cell.action, cell);
                      }
                    }}
                    sx={{
                      backgroundColor: cell.backgroundColor,
                      borderRight: (showBorders && cell.showBorder !== false && colIndex < row.length - 1)
                        ? '1px solid rgba(224, 224, 224, 1)'
                        : undefined,
                      cursor: cell.action ? 'pointer' : undefined,
                      ...textStyleToCss(cellTextStyle),
                      ...modifierToSx(cell.modifier),
                    }}
                  >
                    {cell.text}
                  </TableCell>
                );
              })}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};
