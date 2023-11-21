import React from 'react';
import RouterWeb from './RouterWeb';
import Footer from './components/Footer';
import Header from './components/Header';
import Home from './pages/Home';
import './App.css';

function App() {
  return (
    <div className="App">
        <Header/>
        <RouterWeb/>
        <Footer/>
    </div>
  );
}

export default App;
