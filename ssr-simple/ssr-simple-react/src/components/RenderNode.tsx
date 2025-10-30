import React from 'react';
import { Typography } from '@mui/material';
import { NodeModel } from '../models';
import { RenderScaffold } from './RenderScaffold';
import { RenderTopBar } from './RenderTopBar';
import { RenderColumn } from './RenderColumn';
import { RenderRow } from './RenderRow';
import { RenderBox } from './RenderBox';
import { RenderCard } from './RenderCard';
import { RenderText } from './RenderText';
import { RenderButton } from './RenderButton';
import { RenderImage } from './RenderImage';
import { RenderTable } from './RenderTable';
import { RenderSpacer } from './RenderSpacer';

export interface RenderNodeProps {
  node: NodeModel;
  onAction?: (action: string, rowData?: any) => void;
}

export const RenderNode: React.FC<RenderNodeProps> = ({ node, onAction = () => {} }) => {
  switch (node.type) {
    case 'Scaffold':
      return <RenderScaffold node={node} onAction={onAction} />;
    case 'TopAppBar':
      return <RenderTopBar node={node} />;
    case 'Column':
      return <RenderColumn node={node} onAction={onAction} />;
    case 'Row':
      return <RenderRow node={node} onAction={onAction} />;
    case 'Box':
      return <RenderBox node={node} onAction={onAction} />;
    case 'Card':
      return <RenderCard node={node} onAction={onAction} />;
    case 'Text':
      return <RenderText node={node} />;
    case 'Button':
      return <RenderButton node={node} onAction={onAction} />;
    case 'Image':
      return <RenderImage node={node} />;
    case 'Table':
      return <RenderTable node={node} onAction={onAction} />;
    case 'Spacer':
      return <RenderSpacer node={node} />;
    default:
      return <Typography>Unknown type: {node.type}</Typography>;
  }
};
