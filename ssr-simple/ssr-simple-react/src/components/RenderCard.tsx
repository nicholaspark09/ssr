import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';
import { NodeModel } from '../models';
import { modifierToSx, textStyleToSx } from '../utils';
import { RenderNode } from './RenderNode';

export interface RenderCardProps {
  node: NodeModel;
  onAction?: (action: string, rowData?: any) => void;
}

export const RenderCard: React.FC<RenderCardProps> = ({ node, onAction = () => {} }) => {
  return (
    <Card
      elevation={node.elevation}
      sx={{
        ...modifierToSx(node.modifier),
        backgroundColor: node.backgroundColor,
      }}
    >
      <CardContent>
        {node.title && (
          <Typography variant="h6" sx={textStyleToSx(node.textStyle)}>
            {node.title}
          </Typography>
        )}
        {node.description && (
          <Typography variant="body2" color="text.secondary">
            {node.description}
          </Typography>
        )}
        {node.children?.map((child, index) => (
          <RenderNode key={index} node={child} onAction={onAction} />
        ))}
      </CardContent>
    </Card>
  );
};
