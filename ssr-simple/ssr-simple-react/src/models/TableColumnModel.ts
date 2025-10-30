import { TextStyleModel } from './TextStyleModel';

export interface TableColumnModel {
  header: string;
  weight?: number; // Column weight for flexible width (e.g., 1, 2)
  width?: number; // Fixed width in px (overrides weight if set)
  horizontalAlignment?: 'start' | 'center' | 'end' | 'left' | 'right';
  textStyle?: TextStyleModel; // Style for this column's cells
  headerStyle?: TextStyleModel; // Style for this column's header
}
