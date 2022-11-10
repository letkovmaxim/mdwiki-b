import React from 'react';
import { BrowserRouter , Route, Routes } from 'react-router-dom';
import { Registration } from './pages/Registration';
import { Login } from './pages/Login';
import { Main } from './pages/Main';
import { Redirect } from './component/Redirect';

function App() {

    function checkLogin() {
        const login = window.localStorage.getItem('login')

        if (login === 'yes') {
           return <Main />
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
                <Route path="/main" element={checkLogin()}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
