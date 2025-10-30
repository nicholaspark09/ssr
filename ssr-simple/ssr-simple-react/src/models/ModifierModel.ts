export interface ModifierModel {
  height?: number;
  width?: number;
  weight?: number; // For Row/Column children: proportional size
  padding?: number;
  paddingTop?: number;
  paddingBottom?: number;
  paddingStart?: number;
  paddingEnd?: number;
  fillMaxSize?: boolean;
  fillMaxWidth?: boolean;
  backgroundColor?: string;
  horizontalAlignment?: 'start' | 'center' | 'end';
  verticalAlignment?: 'top' | 'center' | 'bottom';
  contentAlignment?: 'topStart' | 'topCenter' | 'topEnd' | 'centerStart' | 'center' | 'centerEnd' | 'bottomStart' | 'bottomCenter' | 'bottomEnd';
  verticalScroll?: boolean; // Enable vertical scrolling for Column
}
