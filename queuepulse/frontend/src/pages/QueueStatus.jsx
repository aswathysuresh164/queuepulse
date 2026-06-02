import { useEffect, useState } from 'react';
import { fetchQueues, joinQueue } from '../api/queues';

export default function QueueStatus() {
  const [queues, setQueues] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const [joiningId, setJoiningId] = useState(null);
  const [lastToken, setLastToken] = useState(null);

  const loadQueues = async () => {
    setError('');
    try {
      const data = await fetchQueues();
      setQueues(data);
    } catch (err) {
      setError(err.response?.data?.detail || 'Failed to load queues');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadQueues();
  }, []);

  const handleJoin = async (queueId) => {
    setJoiningId(queueId);
    setError('');
    try {
      const result = await joinQueue(queueId);
      setLastToken(result);
    } catch (err) {
      setError(err.response?.data?.detail || 'Could not join queue');
      setLastToken(null);
    } finally {
      setJoiningId(null);
    }
  };

  if (loading) {
    return <p style={{ color: 'var(--muted)' }}>Loading queues…</p>;
  }

  return (
    <>
      <header className="page-header">
        <h1>Queue Status</h1>
        <p>View queues and join to receive a token</p>
      </header>

      {error && <div className="error-banner">{error}</div>}

      {lastToken && (
        <div
          className="card"
          style={{
            marginBottom: '1.5rem',
            borderColor: 'var(--accent)',
            background: 'rgba(20, 184, 166, 0.08)',
          }}
        >
          <p style={{ margin: 0, color: 'var(--muted)', fontSize: '0.9rem' }}>
            Your token
          </p>
          <p style={{ margin: '0.25rem 0 0', fontSize: '2rem', fontWeight: 700 }}>
            {lastToken.token}
          </p>
          <p style={{ margin: '0.5rem 0 0', color: 'var(--muted)', fontSize: '0.85rem' }}>
            Position #{lastToken.position} · Joined{' '}
            {new Date(lastToken.joinedAt).toLocaleTimeString()}
          </p>
        </div>
      )}

      <div className="card">
        {queues.length === 0 ? (
          <p style={{ color: 'var(--muted)', margin: 0 }}>No queues available.</p>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Queue</th>
                  <th>Status</th>
                  <th>Organization</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {queues.map((q) => (
                  <tr key={q.id}>
                    <td>{q.name}</td>
                    <td>
                      <span className={`badge badge-${q.status.toLowerCase()}`}>
                        {q.status}
                      </span>
                    </td>
                    <td>#{q.organizationId}</td>
                    <td>
                      <button
                        type="button"
                        className="btn-primary"
                        disabled={q.status !== 'ACTIVE' || joiningId === q.id}
                        onClick={() => handleJoin(q.id)}
                      >
                        {joiningId === q.id ? 'Joining…' : 'Join queue'}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </>
  );
}
