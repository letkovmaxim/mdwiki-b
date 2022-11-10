import React from "react";
import "../css/hello.css"
import Contacts from "../component/Button";

export class Hello extends React.Component<any, any>{
    render() {
        return(
            <div>
                <div className="formText">

                    <div className="formLogo">
                        <div className="rectangle1"></div>

                        <div className="ws">
                            WS
                        </div>

                        <div className="rectangle2"></div>
                    </div>

                    <div className="greetingText1">
                        Добро пожаловать в наш wiki-сервис!
                    </div>

                    <div className="greetingText2">
                        В нем ты можешь создавать свои записи
                        и делиться ими с коллегами и друзьями
                    </div>
                </div>
                <Contacts/>
            </div>
        );
    }
}