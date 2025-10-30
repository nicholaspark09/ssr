import React from 'react';
import { Box, Fab } from '@mui/material';
import { NodeModel } from '../models';
import { RenderNode } from './RenderNode';

export interface RenderScaffoldProps {
  node: NodeModel;
  onAction?: (action: string, rowData?: any) => void;
}

export const RenderScaffold: React.FC<RenderScaffoldProps> = ({ node, onAction = () => {} }) => {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
      {node.topBar && <RenderNode node={node.topBar} onAction={onAction} />}

      <Box sx={{ flex: 1, overflow: 'auto' }}>
        {node.content && <RenderNode node={node.content} onAction={onAction} />}
      </Box>

      {node.floatingActionButton && (
        <Fab
          color="primary"
          sx={{ position: 'fixed', bottom: 16, right: 16 }}
          onClick={() => node.floatingActionButton?.action && onAction(node.floatingActionButton.action)}
        >
          {node.floatingActionButton.label || '+'}
        </Fab>
      )}
    </Box>
  );
};
