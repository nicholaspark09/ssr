import React from 'react';
import { Box } from '@mui/material';
import { NodeModel } from '../models';
import { modifierToSx } from '../utils';

export interface RenderSpacerProps {
  node: NodeModel;
}

export const RenderSpacer: React.FC<RenderSpacerProps> = ({ node }) => {
  return <Box sx={modifierToSx(node.modifier)} />;
};
