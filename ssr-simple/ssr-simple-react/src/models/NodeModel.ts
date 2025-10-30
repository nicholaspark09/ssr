import { ModifierModel } from './ModifierModel';
import { TextStyleModel } from './TextStyleModel';
import { TableColumnModel } from './TableColumnModel';
import { TableCellModel } from './TableCellModel';

export interface NodeModel {
  type: string;
  title?: string;
  description?: string;
  label?: string;
  backgroundColor?: string;
  elevation?: number;
  imageUrl?: string;
  imageHeight?: number;
  imageWidth?: number;
  contentDescription?: string;
  action?: string;
  modifier?: ModifierModel;
  textStyle?: TextStyleModel;
  topBar?: NodeModel;
  floatingActionButton?: NodeModel;
  content?: NodeModel;
  children?: NodeModel[];

  // Button-specific properties
  buttonVariant?: 'filled' | 'outlined' | 'text';

  // Table-specific properties
  columns?: TableColumnModel[];
  tableData?: TableCellModel[][];
  showBorders?: boolean;
  headerBackgroundColor?: string;
  rowAction?: string; // Action when entire row is clicked
  roundedCorners?: number; // Rounded corner radius in px for tables
  useLazyColumn?: boolean; // Use virtualization for table rows (default: true)
}
