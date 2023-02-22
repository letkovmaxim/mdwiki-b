import React from "react";
import { Container, Form, FormGroup, Input} from "reactstrap";
import {Hello} from '../component/Hello'
import Button from '@mui/material/Button';
import {personLogIn} from "../redux/actions";
import {connect} from "react-redux";
import Box from "@mui/material/Box";
import "../css/loginAndRegistratiom.css"

class Login extends React.Component<any, any>{
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

        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(person),
        })

        if(response.ok){
            this.props.personLogIn()
            window.location.replace('/wiki');
        }else{
            this.setState({
                message: "Неправильный логин или пароль"
            })
        }
    }

    handleSubmitToRegistration(){
        window.location.replace('/registration');
    }

    render() {
        const {person} = this.state;

        return(
            <div>
                <Hello/>
                <Container className="formInput">
                    <Button
                        className="!absolute right-3 !mt-3 styleBut"
                        variant="contained"
                        size="small"
                        onClick={this.handleSubmitToRegistration}
                    >
                        РЕГИСТРАЦИЯ
                    </Button>

                    <Form onSubmit={this.handleSubmit}>

                        <Box className='boxInput'>
                            <Box  className='space-y-[45px]'>
                                <FormGroup>
                                    <Input className="input" type="text" name="usernameOrEmail" placeholder="ЛОГИН ИЛИ EMAIL" id="usernameOrEmail" value={person.usernameOrEmail}
                                           onChange={this.handleChange} autoComplete="off"/>
                                </FormGroup>

                                <Box className='h-[9vh]'>
                                    <FormGroup>
                                        <Input className="input" type="password" name="password" placeholder="ПАРОЛЬ" id="password" value={person.password}
                                               onChange={this.handleChange} autoComplete="off"/>
                                    </FormGroup>

                                    <div className='textError'>
                                        {this.state.message}
                                    </div>
                                </Box>
                            </Box>
                        </Box>

                        <Button
                            className="loginBut styleBut"
                            variant="contained"
                            size="medium"
                            type="submit">
                            &emsp;ВОЙТИ&emsp;
                        </Button>
                    </Form>
                </Container>
            </div>
        );
    }
}

const mapDispatchToProps = {
    personLogIn
}

const mapStateToProps = (state:any) =>{
    return {
        isLogin: state.app.loading
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Login)