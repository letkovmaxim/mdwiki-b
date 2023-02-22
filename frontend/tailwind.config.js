/** @type {import('tailwindcss').Config} */
const plugin = require('tailwindcss/plugin');

module.exports = {
  mode: 'jit',
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      fontSize: {
        vw1: '1vw'
      },
      colors: {
        blue: {
          450: '#70CCF2',
          470: '#4FB5D7'
        },
      },
      fontFamily: {
        'algerian': ['algerian'],
        'montserrat': ['Montserrat'],
        'scada': ['Scada']
      }
    },
  },
  plugins: [
    plugin(({ addBase, theme }) => {
      addBase({
        '.scrollbar': {
          overflowY: 'auto',
          height: 'calc(100vh - 230px)',
          maxWidth: '300px',
          marginTop: '10px'
        },
        '.scrollbar::-webkit-scrollbar': {
          height: '5px',
          width: '5px',
        },
        '.scrollbar::-webkit-scrollbar-track': {
          backgroundColor: 'white',
        },
        '.scrollbar::-webkit-scrollbar-thumb': {
          backgroundColor: '#A4A9AE',
          borderRadius: '20px'
        },

        '.documentation' : {
          'height': '100%',
          'marginTop': '10px',
          'width': '100%',
          'overflow-y': 'auto'
        },
        '.documentation::-webkit-scrollbar' : {
          'width': '5px',
          'height': '5px'
        },
        '.documentation::-webkit-scrollbar-track' : {
          'background': 'white'
        },
        '.documentation::-webkit-scrollbar-thumb' : {
          'background-color': '#A4A9AE',
          'borderRadius': '20px'
        },

        '.imagesPreview' : {
          'height': '*',
          'width': '550',
          'position': 'absolute',
          'maxHeight': '300px',
          'overflow-y': 'auto'
        },
        '.imagesPreview::-webkit-scrollbar' : {
          'width': '5px',
          'height': '5px'
        },
        '.imagesPreview::-webkit-scrollbar-track' : {
          'background': 'white'
        },
        '.imagesPreview::-webkit-scrollbar-thumb' : {
          'background-color': '#A4A9AE',
          'borderRadius': '20px'
        }
      });
    }),
  ],
}
