import React from 'react';
import { Typography } from '@mui/material';
import { NodeModel } from '../models';
import { modifierToSx, textStyleToSx } from '../utils';

export interface RenderTextProps {
  node: NodeModel;
}

export const RenderText: React.FC<RenderTextProps> = ({ node }) => {
  const text = node.title || node.description || node.label || '';

  return (
    <Typography
      sx={{
        ...modifierToSx(node.modifier),
        ...textStyleToSx(node.textStyle),
      }}
    >
      {text}
    </Typography>
  );
};
