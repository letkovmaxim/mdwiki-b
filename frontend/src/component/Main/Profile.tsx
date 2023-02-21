import React, {useEffect, useState} from "react";
import PersonIcon from "@mui/icons-material/Person";
import Button from "@mui/material/Button"
import Modal from '@mui/material/Modal';
import Box from "@mui/material/Box";
import {Input} from "reactstrap";
import "../../css/main.css"
import {useDispatch} from "react-redux";
import {logOut} from "../../redux/actions";

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '500px',
    height: '250px',
    backgroundColor: '#4FB5D7',
    borderStyle: 'solid',
    borderWidth: 3,
    borderColor: '#FCFCFC',
    borderRadius: 5,
    boxShadow: 24,
    p: 2,
    outline: 'none'
};

export const Profile = () => {

    const dispatch = useDispatch()

    const [person, setPerson] = useState({
        id: 0,
        username: '',
        password: '',
        name: '',
        email: '',
        enabled: true
    })

    const [nameError, setNameError] = useState('')
    const [emailError, setEmailError] = useState('')

    const [name, setName] = useState('')
    const [email, setEmail] = useState('')

    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => {
        setOpen(false);
        setName(person.name)
        setEmail(person.email)
        setNameError('')
        setEmailError('')
    }

    useEffect(() => {
        getPerson()
    }, [])

    async function  getPerson() {
        let response = await fetch("/auth/whoami");
        let json = await response.json()
        if(response.ok){
            setPerson({
                id: json.id,
                username: json.username,
                password: 'nonenone',
                name: json.name,
                email: json.email,
                enabled: json.enabled
            })
            setName(json.name)
            setEmail(json.email)
        }else {
            dispatch(logOut())
        }
    }

    const handleChange = (e:any) => {
        if(e.target.name === 'name'){
            setName(e.target.value)
        }else if(e.target.name === 'email'){
            setEmail(e.target.value)
        }
    }

    const personUpdateLogin = async () =>{
        if(!loginValid(name)){
            let response = await fetch('/people/' + person.id, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({...person, name: name}),
            })

            if(response.ok){
                await getPerson()
            }
        }
    }

    const loginValid = (name:string) => {
        setNameError('')

        if(!name.length){
            setNameError("Поле не должно быть пустым")
            return true
        }else if (name === person.name){
            return true
        }
    }

    const personUpdateEmail = async () => {
        if(!emailValid(email)){
            let response = await fetch('/people/' + person.id, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({...person, email: email}),
            })

            if(response.ok){
                await getPerson()
            }else{
                setEmailError("Такой email уже существует")
            }
        }
    }

    const emailValid = (email:string) => {
        setEmailError('')

        if(!email.length){
            setEmailError("Поле не должно быть пустым")
            return true
        }else if (email === person.email){
            return true
        }
    }

    return(
        <div>
            <Button sx={{alignItems: 'left', justifyContent: 'left'}} color="primary" type="submit" onClick={handleOpen}>
                <PersonIcon sx={{color: '#747A80', height:20}}/>
                <div>&emsp;</div>
                <div style={{color: '#747A80'}}>
                    ПРОФИЛЬ
                </div>
            </Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Box className='titleBoxProfile'>
                        <PersonIcon className='titlePersonIcon'/>
                        <div className='titlePersonName'>
                            {person.username}
                        </div>
                    </Box>
                    <Box className='boxEdit'>
                        <Box>
                            <Input
                                className='textField'
                                type="text"
                                name="name"
                                id="username"
                                value={name}
                                onChange={handleChange}
                                placeholder="Имя"
                                autoComplete="off"
                            />
                            <Button style={{color: '#FFFFFF', fontWeight: 'bold', marginLeft: '15px'}} variant="text" onClick={personUpdateLogin}>ИЗМЕНИТЬ</Button>
                            <div style={{color:"red", fontWeight: "bold"}}>
                                {nameError}
                            </div>
                        </Box>

                        <Box style={{position: 'absolute', top: '65px'}}>
                            <Input
                                className='textField'
                                type='text'
                                name="email"
                                id="email"
                                value={email}
                                onChange={handleChange}
                                placeholder="Email"
                                autoComplete="off"
                            />
                            <Button style={{color: '#FFFFFF', fontWeight: 'bold', marginLeft: '15px'}} variant="text" onClick={personUpdateEmail}>ИЗМЕНИТЬ</Button>
                            <div style={{color:"red", fontWeight: "bold"}}>
                                {emailError}
                            </div>
                        </Box>
                    </Box>
                </Box>
            </Modal>
        </div>
    )
}