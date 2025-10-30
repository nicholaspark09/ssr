import React from 'react';
import { Box } from '@mui/material';
import { NodeModel } from '../models';
import { modifierToSx } from '../utils';

export interface RenderImageProps {
  node: NodeModel;
}

export const RenderImage: React.FC<RenderImageProps> = ({ node }) => {
  const imageStyle: React.CSSProperties = {};

  if (node.imageWidth !== undefined) imageStyle.width = node.imageWidth;
  if (node.imageHeight !== undefined) imageStyle.height = node.imageHeight;

  return (
    <Box sx={modifierToSx(node.modifier)}>
      <img
        src={node.imageUrl}
        alt={node.contentDescription || ''}
        style={imageStyle}
      />
    </Box>
  );
};
