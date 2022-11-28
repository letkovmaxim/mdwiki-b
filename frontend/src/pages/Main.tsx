import React from 'react';
import {Container} from "reactstrap";
import Button from "@mui/material/Button";
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import "../css/main.css"
import Contacts from "../component/Button";
import user from "../images/user.svg"
import PopupState, { bindTrigger, bindMenu } from 'material-ui-popup-state';

export class Main extends React.Component<any, any> {

    authPerson = {
        id: '',
        username: '',
        name: '',
        email: '',
        createdAt: '',
        updateAt: '',
        enabled: '',
        role: ''
    };

    constructor(props:any) {
        super(props);
        this.state = {
            person: this.authPerson
        };
    }

    componentDidMount() {
        this.ifLogIn()
    }

    async ifLogIn() {
        let response = await fetch("/auth/whoami");
        let json = await response.json()
        this.setState({
            id: json.id,
            username: json.username,
            name: json.name,
            email: json.email,
            createdAt: json.createdAt,
            updateAt: json.updateAt,
            enabled: json.enabled,
            role: json.role})
    }

    async handleSubmitToLogout(){
        await fetch('/auth/logout', {
            method: 'POST',
        });
        window.localStorage.setItem('login', 'no')
        window.location.replace('/');
    }

    render() {
        return (
            <div className='background'>

                <Contacts/>

                <Container className="logo">
                    <div className="rec1"></div>

                    <div className="logoText">
                        WS
                    </div>

                    <div className="rec2"></div>
                </Container>

                <PopupState variant="popover" popupId="demo-popup-menu">
                    {(popupState) => (
                        <React.Fragment>
                            <Button
                                sx={{
                                    position: 'absolute',
                                    marginTop: '0.1%',
                                    right: '1%'
                                }}
                                variant="text"
                                {...bindTrigger(popupState)}
                            >
                                <img className="img" src={user}/>
                            </Button>
                            <Menu {...bindMenu(popupState)}>
                                <MenuItem onClick={popupState.close}>{this.state.username}</MenuItem>
                                <MenuItem onClick={popupState.close}>
                                    <Button color="primary" type="submit" onClick={this.handleSubmitToLogout}>
                                        <div style={{color: '#F34646'}}>
                                            ВЫХОД
                                        </div>
                                    </Button>
                                </MenuItem>
                            </Menu>
                        </React.Fragment>
                    )}
                </PopupState>
            </div>
        );
    }
}