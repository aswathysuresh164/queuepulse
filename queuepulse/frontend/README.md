# QueuePulse Frontend

React + Vite client for QueuePulse.

## Stack

- React 18
- Vite 6
- React Router 6
- Axios

## Pages

| Route | Page |
|-------|------|
| `/login` | Sign in |
| `/register` | Create account |
| `/dashboard` | Analytics overview |
| `/queue-status` | View queues & join |

## Run

1. Start the backend on `http://localhost:8080`
2. Install and run:

```bash
cd queuepulse/frontend
npm install
npm run dev
```

Open [http://localhost:5173](http://localhost:5173)

API calls are proxied to the backend (see `vite.config.js`).
