import client from './client';

export async function fetchAnalytics({ queueId, organizationId } = {}) {
  const params = {};
  if (queueId) params.queueId = queueId;
  if (organizationId) params.organizationId = organizationId;
  const { data } = await client.get('/api/v1/analytics', { params });
  return data;
}
