import React from 'react';
import { BrowserRouter , Route, Routes } from 'react-router-dom';
import { Registration } from './pages/Registration';
import { Login } from './pages/Login';
import { Redirect } from './component/Redirect';
import {NotFound} from "./pages/NotFound";
import MainPage from "./pages/Main";

function App() {

    function checkLogin() {
        const login = window.localStorage.getItem('login')

        if (login === 'yes') {
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
                <Route path="/wiki/:login" element={checkLogin()}/>
                <Route path="/wiki/:login/space/:spaceId" element={checkLogin()}/>
                <Route path="/wiki/:login/space/:spaceId/page/:pageId" element={checkLogin()}/>
                <Route path="/*" element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
