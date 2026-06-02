import client from './client';

export async function fetchQueues(organizationId) {
  const params = organizationId ? { organizationId } : {};
  const { data } = await client.get('/api/v1/queues', { params });
  return data;
}

export async function fetchQueue(id) {
  const { data } = await client.get(`/api/v1/queues/${id}`);
  return data;
}

export async function joinQueue(queueId) {
  const { data } = await client.post(`/api/v1/queues/${queueId}/join`);
  return data;
}
