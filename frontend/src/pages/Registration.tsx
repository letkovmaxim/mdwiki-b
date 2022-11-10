import React from "react";
import {Container, Form, FormGroup, Input} from "reactstrap";
import {Hello} from './Hello'
import '../css/registration.css'
import Button from '@mui/material/Button';

const styleButton = {
    backgroundColor: '#70CCF2',
    borderStyle: 'solid',
    borderWidth: 3,
    borderColor: '#FCFCFC',
    borderRadius: 20,
};

export class Registration extends React.Component<any, any>{

    newPerson = {
        username: '',
        password: '',
        name: '',
        email: '',
        isEnabled: true
    };

    constructor(props: any) {
        super(props);
        this.state = {
            person: this.newPerson,
            repeatPassword: ''
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChangeRepeatPassword = this.handleChangeRepeatPassword.bind(this);
    }

    handleChange(event: any) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let person = {...this.state.person};
        person[name] = value;
        this.setState({person});
    }

    handleChangeRepeatPassword(event: any){
        this.setState({
            [event.target.name]: event.target.value
        });
    }

    async handleSubmit(event: any) {
        event.preventDefault();
        const {person} = this.state;

        if(!this.errorEmpty()){
            await fetch('/auth/registration', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type':  'application/json'
                },
                body: JSON.stringify(person),
            })
                .then(async response => {
                    let json = await response.json()
                    if (!response.ok) {
                        this.setState({
                            message: json.errors
                        })
                        this.error(json.errors)
                    }else {
                        window.location.replace('/login');
                    }});
        }
    }

    errorEmpty(){
        const {person} = this.state;
        const {repeatPassword} = this.state;

        this.setState({
            checkUsername: '',
            checkEmail: '',
            checkName: '',
            checkPassword: '',
            checkRepeatPassword: ''
        })

        let isError = false;

        if(person.name.length === 0){
            this.setState({
                checkName: "Имя не должно быть пустым"
            })
            isError = true;
        }

        if(person.username.length < 3){
            this.setState({
                checkUsername: "Логин не должно быть короче 4 символов"
            })
            isError = true;
        }

        if(person.email.length === 0){
            this.setState({
                checkEmail: "Email не должно быть пустым"
            })
            isError = true;
        }

        if(person.password.length < 5){
            this.setState({
                checkPassword: "Пароль не должен быть короче 6 символов"
            })
            isError = true;
        }

        if(person.password !== repeatPassword){
            this.setState({
                checkRepeatPassword: "Пароли не совпадают"
            })
            isError = true;
        }

        return isError;
    }

    error(errors:any){

        this.setState({
            checkUsername: '',
            checkEmail: ''
        })

        errors.forEach((entry: any) => {
            if(entry.includes("логин")){
                this.setState({
                    checkUsername: entry
                })
            }else if(entry.includes("Email") || entry.includes("email")){
                this.setState({
                    checkEmail: entry
                })
            }
        })
    }

    handleSubmitToLogin(){
        window.location.replace('/login');
    }

    render() {
        const {person} = this.state;
        const {repeatPassword} = this.state;

        return(
            <div>
                <Hello/>
                <Container className="formReg">
                    <Button
                        sx={{
                            position: 'absolute',
                            right: '3%',
                            marginTop: '2.5%',
                        }}
                        style={styleButton}
                        variant="contained"
                        size="small"
                        onClick={this.handleSubmitToLogin}
                    >
                        <div className="butText">
                            &emsp;ВХОД&emsp;
                        </div>
                    </Button>

                    <Form onSubmit={this.handleSubmit}>
                        <FormGroup>
                            <Input className="name" type="text" name="name" placeholder="Имя" id="name" value={person.name}
                                   onChange={this.handleChange} autoComplete="name"/>
                            <Container className="errorName">
                                <div className="errText">
                                    {this.state.checkName}
                                </div>
                            </Container>
                        </FormGroup>

                        <FormGroup>
                            <Input className="userName" type="text" name="username" placeholder="Логин" id="username" value={person.username}
                                   onChange={this.handleChange} autoComplete="username"/>
                            <Container className="errorUserName">
                                <div className="errText">
                                    {this.state.checkUsername}
                                </div>
                            </Container>
                        </FormGroup>

                        <FormGroup>
                            <Input className="email" type="text" name="email" placeholder="Email" id="email" value={person.email}
                                   onChange={this.handleChange} autoComplete="email"/>
                            <Container className="errorEmail">
                                <div className="errText">
                                    {this.state.checkEmail}
                                </div>
                            </Container>
                        </FormGroup>

                        <FormGroup>
                            <Input className="passw" type="password" name="password" placeholder="Пароль" id="password" value={person.password}
                                   onChange={this.handleChange} autoComplete="password"/>
                            <Container className="errorPassword">
                                <div className="errText">
                                    {this.state.checkPassword}
                                </div>
                            </Container>
                        </FormGroup>

                        <FormGroup>
                            <Input className="repeatPassword" type="password" name="repeatPassword" placeholder="Повторите пароль" id="repeatPassword" value={repeatPassword}
                                   onChange={this.handleChangeRepeatPassword} autoComplete="repeatPassword"/>
                            <Container className="errorRepeatPassword">
                                <div className="errText">
                                    {this.state.checkRepeatPassword}
                                </div>
                            </Container>
                        </FormGroup>

                        <Container className="registrationButton">
                            <Button
                                sx={{
                                    position: 'absolute',
                                    top: '50%',
                                    left: '50%',
                                    marginRight: '-50%',
                                    transform: 'translate(-50%, -50%)'
                                }}
                                style={styleButton}
                                variant="contained"
                                size="medium"
                                type="submit">
                                <div className="buttonReg">
                                    ЗАРЕГИСТРИРОВАТЬСЯ
                                </div>
                            </Button>
                        </Container>
                    </Form>
                </Container>
            </div>
        );
    }
}