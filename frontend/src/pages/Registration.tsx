import React from "react";
import {Container, Form, FormGroup, Input} from "reactstrap";
import {Hello} from '../component/Hello'
import Button from '@mui/material/Button';
import "../css/loginAndRegistratiom.css"
import Box from "@mui/material/Box";

export class Registration extends React.Component<any, any>{

    newPerson = {
        username: '',
        password: '',
        name: '',
        email: ''
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
                    if (response.status === 409) {
                        this.setState({
                            message: json.errors
                        })
                        this.error(json.errors)
                    }else if(response.status === 400){
                        this.error(["Email введен неккоректно"])
                    }else if(response.status === 201){
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

        if(person.username.length <= 3){
            this.setState({
                checkUsername: "Логин не должен быть короче 4 символов"
            })
            isError = true;
        }

        if(person.email.length === 0){
            this.setState({
                checkEmail: "Email не должен быть пустым"
            })
            isError = true;
        }

        if(person.password.length <= 5){
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
            }else if(entry.includes("Email") || entry.includes("адресом")){
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
                <Container className="formInput">
                    <Button
                        className="!absolute right-3 !mt-3 styleBut"
                        variant="contained"
                        size="small"
                        onClick={this.handleSubmitToLogin}
                    >
                        &emsp;ВХОД&emsp;
                    </Button>

                    <Form onSubmit={this.handleSubmit}>
                        <Box className='boxInput'>
                            <Box className='space-y-[30px]'>
                                <FormGroup className='h-[9vh]'>
                                    <Input className="input" type="text" name="name" placeholder="Имя" id="name" value={person.name}
                                           onChange={this.handleChange} autoComplete="name"/>

                                    <div className="textError">
                                        {this.state.checkName}
                                    </div>
                                </FormGroup>

                                <FormGroup className='h-[9vh]'>
                                    <Input className="input" type="text" name="username" placeholder="Логин" id="username" value={person.username}
                                           onChange={this.handleChange} autoComplete="username"/>

                                    <div className="textError">
                                        {this.state.checkUsername}
                                    </div>
                                </FormGroup>

                                <FormGroup className='h-[9vh]'>
                                    <Input className="input" type="text" name="email" placeholder="Email" id="email" value={person.email}
                                           onChange={this.handleChange} autoComplete="email"/>

                                    <div className="textError">
                                        {this.state.checkEmail}
                                    </div>
                                </FormGroup>

                                <FormGroup className='h-[9vh]'>
                                    <Input className="input" type="password" name="password" placeholder="Пароль" id="password" value={person.password}
                                           onChange={this.handleChange} autoComplete="password"/>

                                    <div className="textError">
                                        {this.state.checkPassword}
                                    </div>
                                </FormGroup>

                                <FormGroup className='h-[9vh]'>
                                    <Input className="input" type="password" name="repeatPassword" placeholder="Повторите пароль" id="repeatPassword" value={repeatPassword}
                                           onChange={this.handleChangeRepeatPassword} autoComplete="repeatPassword"/>

                                    <div className="textError">
                                        {this.state.checkRepeatPassword}
                                    </div>
                                </FormGroup>
                            </Box>
                        </Box>

                        <Box className='regBox'>
                            <Button
                                className="regBut styleBut"
                                variant="contained"
                                size="medium"
                                type="submit">
                                ЗАРЕГИСТРИРОВАТЬСЯ
                            </Button>
                        </Box>
                    </Form>
                </Container>
            </div>
        );
    }
}