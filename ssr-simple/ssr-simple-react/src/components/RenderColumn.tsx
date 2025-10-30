import React from 'react';
import { Box } from '@mui/material';
import { NodeModel } from '../models';
import { modifierToSx } from '../utils';
import { RenderNode } from './RenderNode';

export interface RenderColumnProps {
  node: NodeModel;
  onAction?: (action: string, rowData?: any) => void;
}

export const RenderColumn: React.FC<RenderColumnProps> = ({ node, onAction = () => {} }) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        ...modifierToSx(node.modifier),
        backgroundColor: node.backgroundColor || node.modifier?.backgroundColor,
      }}
    >
      {node.children?.map((child, index) => (
        <RenderNode key={index} node={child} onAction={onAction} />
      ))}
    </Box>
  );
};
