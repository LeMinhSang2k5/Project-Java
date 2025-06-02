import {
    Routes,
    Route
} from 'react-router-dom';

import App from "./App";
import HomePage from "./components/Home/HomePage";
import AboutPage from './pages/AboutPage';
import NewHealthProfile from './components/HealthProfile/NewHealthProfile';
import BlogPage from './pages/BlogPage';
import Medical from './pages/Medical';


const Layout = (props) => {
    return (
        <>
            <Routes>
                <Route path="/" element={<App />}>
                    <Route index element={<HomePage />} />
                    <Route path="/about" element={<AboutPage />} />
                    <Route path="/blog" element={<BlogPage />} />
                    <Route path="/health-profile/new" element={<NewHealthProfile />} />
                    <Route path="/medical" element={<Medical />} />
                </Route>
            </Routes>
        </>
    )
}

export default Layout;