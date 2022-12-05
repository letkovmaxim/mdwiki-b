import * as React from 'react';
import { styled, useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import "../css/main.css"
import { useParams } from "react-router-dom";
import {useState, useEffect} from "react";
import {Header} from "../component/Header";
import Contact from "../component/SIdePanel/Contact";
import {SidePanel} from "../component/SIdePanel/SidePanel";
import {Document} from "../component/Markdown/Document";
import List from "@mui/material/List";
import Button from "@mui/material/Button";
import {Note} from "../component/Markdown/Note";

const drawerWidth = 300;

const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' })<{
    open?: boolean;
}>(({ theme, open }) => ({
    flexGrow: 1,
    padding: theme.spacing(3),
    transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    marginLeft: `-${drawerWidth}px`,
    ...(open && {
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
        marginLeft: 0,
    }),
}));

const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
}));


export default function MainPage() {

    const { login } = useParams();

    const {pageId} = useParams();


    useEffect(() => {
        if(window.localStorage.getItem('name') === ''){
            ifLogIn()
        }else{
            if (!(login === window.localStorage.getItem('name') || login === window.localStorage.getItem('email'))) {
                window.location.replace('/404');
            }
        }
    }, [])

    async function ifLogIn() {
        let response = await fetch("/auth/whoami");
        let json = await response.json()
        window.localStorage.setItem('name', json.username)
        window.localStorage.setItem('email', json.email)

        if (!(login === window.localStorage.getItem('name') || login === window.localStorage.getItem('email'))) {
            window.location.replace('/404');
        }
    }

    async function handleSubmitToLogout(){
        await fetch('/auth/logout', {
            method: 'POST',
        });
        window.localStorage.setItem('login', 'no')
        window.location.replace('/');
    }

    const theme = useTheme();
    const [open, setOpen] = React.useState(true);

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    const toNote = () => {
        window.location.replace("/wiki/" + login);
    }

    return (
        <Box sx={{ display: 'flex' }} className='background'>
            <Header
                handleDrawerOpen={handleDrawerOpen}
                open={open}
                handleSubmitToLogout={handleSubmitToLogout}
            />
            <Drawer
                sx={{
                    width: drawerWidth,
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        width: drawerWidth,
                        boxSizing: 'border-box',
                    },
                }}
                variant="persistent"
                anchor="left"
                open={open}
            >
                <DrawerHeader>
                    <Contact/>
                    <IconButton onClick={handleDrawerClose}>
                        {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
                    </IconButton>
                </DrawerHeader>
                <Divider />
                <List>
                    <Button sx={{width:'100%'}} className="btn" variant="text" onClick={toNote}>
                        <div className='textBtn'>
                            Заметки
                        </div>
                    </Button>

                </List>
                <Divider />
                <SidePanel/>
            </Drawer>

            <Main open={open} >
                <DrawerHeader />
                {(pageId ? <Document/> : <Note/>)}
            </Main>
        </Box>
    );
}