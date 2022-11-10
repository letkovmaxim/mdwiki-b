import React, { Component } from 'react';

export class Redirect extends Component {

    componentDidMount(){
        window.location.replace('/login');
    }

    render() {
        return (
            <div/>
        );
    }
}