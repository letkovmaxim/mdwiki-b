import React, {useEffect} from 'react';
import {useSelector} from "react-redux";

export const Redirect = () => {

    const isLogin = useSelector((state:any) => state.app.login)

    useEffect(() => {
        if(isLogin){
            window.location.replace('/wiki');
        }else {
            window.location.replace('/login');
        }
    })

    return (
        <></>
    );
}