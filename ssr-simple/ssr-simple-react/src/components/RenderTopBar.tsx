import React from 'react';
import { AppBar, Toolbar, Typography } from '@mui/material';
import { NodeModel } from '../models';
import { textStyleToSx } from '../utils';

export interface RenderTopBarProps {
  node: NodeModel;
}

export const RenderTopBar: React.FC<RenderTopBarProps> = ({ node }) => {
  return (
    <AppBar position="static" sx={{ backgroundColor: node.backgroundColor }}>
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1, ...textStyleToSx(node.textStyle) }}>
          {node.title || ''}
        </Typography>
      </Toolbar>
    </AppBar>
  );
};
