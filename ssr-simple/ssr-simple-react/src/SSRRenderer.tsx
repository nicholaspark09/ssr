import React from 'react';
import { RenderNode } from './components';
import { NodeModel } from './models';

export interface SSRRendererProps {
  jsonString?: string;
  nodeModel?: NodeModel;
  onAction?: (action: string, data?: any) => void;
}

/**
 * Main component for rendering server-side JSON into React components
 */
export const SSRRenderer: React.FC<SSRRendererProps> = ({ jsonString, nodeModel, onAction = () => {} }) => {
  const [error, setError] = React.useState<string | null>(null);
  const [parsedNode, setParsedNode] = React.useState<NodeModel | null>(null);

  React.useEffect(() => {
    if (nodeModel) {
      setParsedNode(nodeModel);
      setError(null);
    } else if (jsonString) {
      try {
        const parsed = JSON.parse(jsonString) as NodeModel;
        setParsedNode(parsed);
        setError(null);
      } catch (err) {
        setError(`Failed to parse JSON: ${err}`);
        setParsedNode(null);
      }
    }
  }, [jsonString, nodeModel]);

  if (error) {
    return <div style={{ color: 'red', padding: 16 }}>{error}</div>;
  }

  if (!parsedNode) {
    return <div style={{ padding: 16 }}>No content to display</div>;
  }

  return <RenderNode node={parsedNode} onAction={onAction} />;
};
