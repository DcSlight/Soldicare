# Soldicare

Frontend project built with [Vite](https://vitejs.dev/), [React](https://react.dev/), and [TypeScript](https://www.typescriptlang.org/).

## Getting Started

Follow these steps to run the project locally.

### Prerequisites

- [Node.js](https://nodejs.org/) version **>=18.x**
- [npm](https://www.npmjs.com/) (comes with Node.js)

To check your installed versions, run:

```bash
node -v
npm -v
```

> ⚠️ Make sure your Node.js version is **18 or above**. If not, install the latest version from [nodejs.org](https://nodejs.org/) or use a version manager like [nvm](https://github.com/nvm-sh/nvm).

### Install Dependencies

In your project root directory, run:

```bash
npm install
```

### Environment Variables

Before running the project, make sure to create a `.env` file in the root of the project with the following required variables:

```env
VITE_API_BACKEND_URL=http://localhost:8084/ambient-intelligence
VITE_APP_NAME=2025b.Itay.Chabra
```

### Start Development Server

Once dependencies are installed, run:

```bash
npm run dev
```

This will start the Vite development server. By default, the app should be available at:

```
http://localhost:5173
```

### Project Structure

```
SOLDICARE/
├── node_modules/         # Installed npm packages (auto-generated, do not edit manually)
├── public/               # Static assets served directly (e.g., images, favicon, robots.txt)
├── src/                  # Main source code of the application
│   ├── assets/           # Static resources like images, icons, fonts, etc.
│   ├── components/       # Reusable UI components (e.g., buttons, modals, inputs)
│   ├── constants/        # Constant values used throughout the app (e.g., strings, config)
│   ├── context/          # React Context API implementations for global state management
│   ├── data/             # Static or mock data for development/testing
│   ├── enums/            # TypeScript enums used across the project
│   ├── hooks/            # Custom React hooks
│   ├── interface/        # TypeScript interfaces and types for data modeling
│   ├── layouts/          # Layout components that wrap pages (e.g., MainLayout)
│   ├── pages/            # Top-level routes/pages in the app (e.g., Home, About)
│   ├── routes/           # Route definitions and configuration
│   ├── services/         # API service calls and external integrations (e.g., axios clients)
│   ├── theme/            # Theme configuration (e.g., colors, typography, MUI theme setup)
│   ├── types/            # Global TypeScript type definitions
│   ├── utils/            # Utility/helper functions
│   ├── App.css           # Global CSS styles
│   ├── App.tsx           # Root React component
│   ├── index.css         # Additional global styles
│   ├── main.tsx          # Entry point of the React app
│   └── vite-env.d.ts     # Vite-specific TypeScript environment declarations
├── .env                  # Environment variables (e.g., API keys, base URLs)

```

### Login - Test

To login to the system you can pick one of the diffrent role:

1. `jane@demo.org` - ADMIN
2. `joanna@demo.org` - OPERATOR
3. `jack@demo.org` - END_USER

Recommended - use `joanna@demo.org`

- use SystemID `2025b.Itay.Chabra`
