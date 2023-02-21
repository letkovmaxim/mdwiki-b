import React from 'react';
import { BrowserRouter , Route, Routes } from 'react-router-dom';
import { Registration } from './pages/Registration';
import Login from './pages/Login';
import { Redirect } from './component/Redirect';
import {NotFound} from "./pages/NotFound";
import MainPage from "./pages/Main";
import {useSelector} from "react-redux";

function App() {

    const isLogin = useSelector((state:any) => state.app.login)

    function checkLogin() {

        if (isLogin) {
           return <MainPage/>
        }else {
            return <Redirect />
        }
    }

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Redirect />} />
                <Route path="/login" element={<Login />} />
                <Route path="/registration" element={<Registration />}/>
                <Route path="/wiki" element={checkLogin()}/>
                <Route path="/wiki/space/:spaceId" element={checkLogin()}/>
                <Route path="/wiki/space/:spaceId/page/:pageId" element={checkLogin()}/>
                <Route path="/*" element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
