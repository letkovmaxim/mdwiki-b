import React from "react";
import "../css/hello.css"
import Contacts from "./Button";
import Box from "@mui/material/Box";

export class Hello extends React.Component<any, any>{
    render() {
        return(
            <div>
                <div className="formHello">
                    <Box className='helloBox'>
                        <Box>
                            <Box className='w-screen'>
                                <div className="rect1"></div>

                                <div className="wsLogo">
                                    WS
                                </div>

                                <div className="rect2"></div>
                            </Box>


                            <Box className='h-1/2 w-screen'>
                                <div className="text1">
                                    Добро пожаловать в наш wiki-сервис!
                                </div>

                                <div className="text2">
                                    В нем ты можешь создавать свои записи
                                    и делиться ими с коллегами и друзьями
                                </div>
                            </Box>
                        </Box>
                    </Box>
                </div>
                <Contacts/>
            </div>
        );
    }
}