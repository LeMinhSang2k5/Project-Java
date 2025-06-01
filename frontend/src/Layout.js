import {
    Routes, 
    Route
} from 'react-router-dom';

import App from "./App";
import HomePage from "./components/Home/HomePage";
import AboutPage from './pages/AboutPage';


const Layout = (props) => {
    return (
        <>
            <Routes>
                <Route path="/" element={<App />}>
                    <Route index element={<HomePage/>} />
                    <Route path="/about" element={<AboutPage/>} />
                </Route>
            </Routes>
        </>
    )
}

export default Layout;