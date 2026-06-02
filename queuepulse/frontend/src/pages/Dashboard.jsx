import { useEffect, useState } from 'react';
import { fetchAnalytics } from '../api/analytics';
import { fetchQueues } from '../api/queues';

function formatWaitTime(seconds) {
  if (seconds == null) return '—';
  const mins = Math.round(seconds / 60);
  if (mins < 60) return `${mins} min`;
  return `${(mins / 60).toFixed(1)} hr`;
}

function formatPeakHour(hour) {
  if (hour == null) return '—';
  const h = hour % 12 || 12;
  const ampm = hour < 12 ? 'AM' : 'PM';
  return `${h}:00 ${ampm}`;
}

export default function Dashboard() {
  const [analytics, setAnalytics] = useState(null);
  const [queues, setQueues] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        const [analyticsData, queuesData] = await Promise.all([
          fetchAnalytics(),
          fetchQueues(),
        ]);
        setAnalytics(analyticsData);
        setQueues(queuesData);
      } catch (err) {
        setError(err.response?.data?.detail || 'Failed to load dashboard');
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  if (loading) {
    return <p className="muted">Loading dashboard…</p>;
  }

  return (
    <>
      <header className="page-header">
        <h1>Dashboard</h1>
        <p>Overview of queue performance today</p>
      </header>

      {error && <div className="error-banner">{error}</div>}

      <section className="stat-grid" style={{ marginBottom: '2rem' }}>
        <div className="stat-card">
          <div className="label">Avg wait time</div>
          <div className="value">
            {formatWaitTime(analytics?.averageWaitingTimeSeconds)}
          </div>
        </div>
        <div className="stat-card">
          <div className="label">Served today</div>
          <div className="value">{analytics?.customersServedToday ?? 0}</div>
        </div>
        <div className="stat-card">
          <div className="label">Peak hour</div>
          <div className="value">{formatPeakHour(analytics?.peakHour)}</div>
        </div>
        <div className="stat-card">
          <div className="label">Peak traffic</div>
          <div className="value">{analytics?.peakHourTraffic ?? '—'}</div>
        </div>
      </section>

      <section className="card">
        <h2 style={{ margin: '0 0 1rem', fontSize: '1.1rem' }}>Active queues</h2>
        {queues.length === 0 ? (
          <p style={{ color: 'var(--muted)', margin: 0 }}>No queues found.</p>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Status</th>
                  <th>Organization</th>
                  <th>Created</th>
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
                    <td>{new Date(q.createdAt).toLocaleDateString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </>
  );
}
