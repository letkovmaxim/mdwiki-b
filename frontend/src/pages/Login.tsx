import React from "react";
import { Container, Form, FormGroup, Input} from "reactstrap";
import "../css/login.css"
import {Hello} from '../component/Hello'
import Button from '@mui/material/Button';

const styleButton = {
    backgroundColor: '#70CCF2',
    borderStyle: 'solid',
    borderWidth: 3,
    borderColor: '#FCFCFC',
    borderRadius: 20,
};

export class Login extends React.Component<any, any>{

    newPerson = {
        usernameOrEmail: '',
        password: ''
    };

    constructor(props: any) {
        super(props);
        this.state = {
            person: this.newPerson,
            message: '',
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event: any) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let person = {...this.state.person};
        person[name] = value;
        this.setState({person});
    }

    async handleSubmit(event: any) {
        event.preventDefault();
        const {person} = this.state;

        await fetch('/auth/login', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(person),
        }).then(response => {
            if (response.status === 200) {
                window.localStorage.setItem('login', 'yes')
                localStorage.setItem("space", '0' )
                localStorage.setItem("spaceName", '' )
                localStorage.setItem("name", '' )
                localStorage.setItem("email", '' )
                localStorage.setItem("tree", JSON.stringify([]))
                window.location.replace('/wiki/' + person.usernameOrEmail);
            }else {
                this.setState({
                    message: "Неправильный логин или пароль"
                })
            }
        });
        console.log(this.state.message);
    }

    handleSubmitToRegistration(){
        window.location.replace('/registration');
    }

    render() {
        const {person} = this.state;

        return(
            <div>
                <Hello/>
                <Container className="formLogin">
                    <Button
                        sx={{
                            position: 'absolute',
                            right: '3%',
                            marginTop: '2.5%',
                        }}
                        style={styleButton}
                        variant="contained"
                        size="small"
                        onClick={this.handleSubmitToRegistration}
                    >
                        <div className="buttonText">
                            РЕГИСТРАЦИЯ
                        </div>
                    </Button>

                    <Form onSubmit={this.handleSubmit}>
                        <FormGroup>
                            <Input className="login" type="text" name="usernameOrEmail" placeholder="ЛОГИН ИЛИ EMAIL" id="usernameOrEmail" value={person.usernameOrEmail}
                                   onChange={this.handleChange} autoComplete="usernameOrEmail"/>
                        </FormGroup>

                        <FormGroup>
                            <Input className="password" type="password" name="password" placeholder="ПАРОЛЬ" id="password" value={person.password}
                                   onChange={this.handleChange} autoComplete="password"/>
                        </FormGroup>

                        <Container className="errorForm">
                            <FormGroup className="errorText">
                                {this.state.message}
                            </FormGroup>
                        </Container>


                        <Button
                            sx={{
                                position: 'absolute',
                                top: '50%',
                                left: '50%',
                                marginRight: '-50%',
                                transform: 'translate(-50%, 500%)'
                            }}
                            style={styleButton}
                            variant="contained"
                            size="medium"
                            type="submit">
                            <div className="buttonSinIN">
                                &emsp;ВОЙТИ&emsp;
                            </div>
                        </Button>

                    </Form>
                </Container>
            </div>
        );
    }
}
