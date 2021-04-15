<template>
  <div  id="nav">
    <a> Bienvenue sur votre éditeur photo en ligne. </a>
    <a class="design" title=":)" >by CeZaHe.</a>
  </div>
  <router-view/>
  
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 10px;
  background-color:slategrey;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
.design{
    float:right;
    cursor:cell;
}


.design:hover{
  
    background-color:red;
}
</style>
