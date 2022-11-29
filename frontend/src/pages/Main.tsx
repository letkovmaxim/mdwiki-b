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
import {Document} from "../component/Document";

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

    const [authPerson, setAuthPerson] = useState({
        id: '',
        username: '',
        name: '',
        email: '',
        createdAt: '',
        updateAt: '',
        isEnabled: '',
        role: '',
    });

    useEffect(() => {
        ifLogIn()
    }, [setAuthPerson])

    async function ifLogIn() {
        let response = await fetch("/auth/whoami");
        let json = await response.json()
        setAuthPerson({
            id: json.id,
            username: json.username,
            name: json.name,
            email: json.email,
            createdAt: json.createdAt,
            updateAt: json.updateAt,
            isEnabled: json.isEnabled,
            role: json.role})

        if (!(login === json.username || login === json.email)) {
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
    const [open, setOpen] = React.useState(false);

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    return (
        <Box sx={{ display: 'flex' }} className='background'>
            <Header
                handleDrawerOpen={handleDrawerOpen}
                open={open}
                handleSubmitToLogout={handleSubmitToLogout}
                username={authPerson.username}
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
                <SidePanel/>
            </Drawer>

            <Main open={open} >
                <DrawerHeader />
                {(pageId ? <Document/> : <div/>)}

            </Main>
        </Box>
    );
}