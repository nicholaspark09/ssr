import { ModifierModel } from './ModifierModel';
import { TextStyleModel } from './TextStyleModel';

export interface TableCellModel {
  text: string;
  textStyle?: TextStyleModel;
  backgroundColor?: string;
  action?: string;
  modifier?: ModifierModel;
  showBorder?: boolean;
}
