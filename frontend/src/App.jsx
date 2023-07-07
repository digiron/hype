import { Routes, Route, Navigate, useNavigate, useLocation } from 'react-router-dom';

import { history } from 'shared';
import { Navigation, AuthenticatedRoute } from 'components';
import { Home } from 'home';
import { Login } from 'login';

export { App };

function App() {
    history.navigate = useNavigate();
    history.location = useLocation();

    return (
        <div className="app-container bg-light">
            <Navigation />
            <div className="container pt-4 pb-4">
                <Routes>
                <Route
                        path="/"
                        element={
                            <AuthenticatedRoute>
                                <Home />
                            </AuthenticatedRoute>
                        }
                    />
                    <Route path="/login" element={<Login />} />
                    <Route path="*" element={<Navigate to="/" />} />
                </Routes>
            </div>
        </div>
    );
}
