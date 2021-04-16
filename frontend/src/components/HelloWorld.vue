<template >

  <div  class="hello" >
    
  <div class="overlay-image">
  <a href="https://www.u-bordeaux.fr/" target="_blank">
    <img class="image" src="../assets/fac.gif" alt="université bordeaux" />
    <div class="hover">
      <div class="text">Site web de l'université de Bordeaux</div>
    </div>
  </a>
  </div>

  <div class="overlay-image2">
  <a href="https://gitlab.emi.u-bordeaux.fr/cezahe/projet-pdl" target="_blank">
    <img class="image"  src="../assets/giphy.gif" alt="projet gitlab" />
    <div class="hover">
      <div class="text">Projet sur GitLab</div>
    </div>
  </a>
  </div>

  <div class="container">
    <div class="large-12 medium-12 small-12 cell">
 
      <label for="file" class="button button1">Choisir une image</label>
      <input name="uploadDocument" type="file" id="file" ref="file" style="display:none" v-on:change="handleFileUpload(),upload() " />
      <span id="up"> (Aucun fichier sélectionné) </span>
      

 
        <button class="button button2" v-on:click="submitFile()">Charger</button>
    </div>
  </div>
  <button class="button button2" v-on:click="removeImage()">SUPPRIMER CETTE IMAGE NULLE</button>
  <div class= "galery">
    <p><i class="arrow right" v-on:click="right()" ></i>
    <img src="" alt="pas d'image chargée" id = "galleryCenter"/>
    <i class="arrow left" v-on:click="left()"></i></p>
  </div>
<div> 

</div>

  <div class="test" >
   <img class="vignettes" v-for="(image,index) in allImages" :value="index" :key="image" :src="image" :alt="pout" v-on:click="choose(index),changeImage()" />
  </div>

  <div class="algorithms">

    <select v-model="currentAlgo" alt="Selectionner un algo" v-on:change="open_button()">
      <option  v-for="(algo, index) in algos" :value ="index" :key="algo"> 
        {{ algo.name }}
      </option>
    </select>
    
    

    <input v-if="currentAlgo == 0 " id="gain" placeholder="Gain">

    <div v-else-if="currentAlgo == 2 ">
      <input id="teinte" placeholder="Teinte (comprise entre 0 et 360)" size = 30 v-on:change="wrongTint()">
      <div class="text_error" id="wrongtint" style="display:none"> Veuillez choisir une valeur entre 0 et 360
      </div>
    </div>

    <div v-else-if="currentAlgo == 3">
      <select  v-model="filterType" alt="Selectionner un filtre">
      <option  v-for="(filtre, index) in filtres" :value ="index" :key="filtre"> 
        {{ filtre.text }}
      </option>
      </select>
      <input  id="filtersize" placeholder="Niveau de flou">
    </div>

    <img id="loader" src="../assets/ajax-loader.gif" alt="potipacman" style="display:none"/>

  </div>  

  <button class="button button2" id="button_algo" v-on:click="algo(listImages[galleryActual].id)">Appliquer l'algorithme</button>

  <a class="button button1"  href="" title="DownloadImage" id="download">
    <img alt="Telecharger l'image">
  </a>

  <button class="button button2" id="button_reset" v-on:click="cancel(0)">Reset</button>
  <button class="button button2" id="button_cancel" v-on:click="cancel(imagePrec.length-1)">Annuler la dernière modification</button>
  <!-- <div class="memebox">
    <div class="meme" v-for="image in allImages" :key="image" >
      <img src=image alt=image>
    </div>
  </div>
   -->
  





</div>





</template>




<script>
import axios from "axios";

export default {
  name: "HelloWorld",

  /*props: {
    msg: String,
  },*/

  data() {
    return {
      selected: '',
      listImages: [],
      galleryActual:0,
      file: '',
      dldImage: "blob",
      galeryImage: "blob",
      allImages:[],
      errors: [],
      pixel: 5,
      algos: [
        {name:'Filtre de Luminosité', text :'increaseLuminosity', value : 0},
        {name :'Egalisation d\'Histograme', text :'histogram', value : 1},
        {name : 'Filtre Coloré',text :'coloredFilter', value : 2},
        {name : 'Filtre de Flou', text :'blurryFilter', value : 3},
        {name: 'Filtre de Bordure (ne marche pas)', text :'borderFilter', value : 4},
      ],
      currentAlgo:0,
      filtres: [
        {text :'gaussien', value : 0},
        {text :'moyen', value : 1},
      ],
      filterType:0,
      displayedImage:0,
      newVign:0,
      imagePrec:[],
      vignettePrec:[],
      hasChangedImage:false,
      imageGettingChanged:0
    };
  },


  methods: {
    left() {
      if (this.galleryActual > 0) {
        this.galleryActual--;
        this.changeImage()
      }
      this.choose(this.galleryActual);
    },
    right() {
      if (this.galleryActual < this.allImages.length-1) {
        this.changeImage()
        this.galleryActual++;
      }
      this.choose(this.galleryActual);
    },

    upload(){
      document.getElementById("up").innerHTML = this.file.name;
    },
    
    changeImage() {
      this.hasChangedImage = true;
    },

    choose(index){
      this.galleryActual = index
      axios.get('images/' + this.listImages[index].id, {responseType: "blob"})
              .then((displayedImage) => {
                var reader = new window.FileReader();
                reader.readAsDataURL(displayedImage.data);
                reader.onload = () => {
                  document.getElementById("galleryCenter").setAttribute("src", reader.result);
                  document.getElementById("download").setAttribute("href", document.getElementById("galleryCenter").src);
                  document.getElementById("download").setAttribute("download", this.listImages[this.galleryActual].name);
                  var img = document.createElement("img");
                    img.onload = () => {
                      if (img.width > img.height) {
                        document.getElementById("galleryCenter").style.width ="50vw"
                        document.getElementById("galleryCenter").style.height ="auto"
                      } else {
                        document.getElementById("galleryCenter").style.width ="auto"
                        document.getElementById("galleryCenter").style.height ="50vh"
                      }
                    }
                  img.src = reader.result
                }
              })
    },



    gallery() {
      this.allImages=[];
      let promises = [];

      function asyncGallery (i) {
        return new Promise((resolve, reject) => {
          try {
            axios.get('images/' + i +"?vignette=true", {responseType: "blob"})
              .then((galeryImage) => {
                var reader = new window.FileReader();
                reader.readAsDataURL(galeryImage.data);
                reader.onload = () => {
                  resolve(reader.result)
                }
              })
              .catch((e) => {
                alert(e);
                this.errors.push(e);
              })

          } catch(e) {
            reject()
          }
        })
      }

    for(let i of this.listImages) {
        promises.push(asyncGallery(i.id))
    }

    Promise.all(promises).then(
        (result) => {
          this.allImages = result
        }
      ).catch(
        (error) =>{
          alert(error)
          this.errors.push(error)
        }
      ).finally(() => {
        this.choose(this.galleryActual);
      })
    },


/*
    changerImage() {
        document.getElementById("galleryCenter").setAttribute("src", this.allImages[this.galleryActual-1])
    },*/




    removeImage() {
      axios
      .delete('images/'+ this.listImages[this.galleryActual].id,{ data: { answer: 42 } })

      .then(() => {
        this.getImages()
        this.listImages.splice(this.galleryActual,1)
        if (this.galleryActual > 0) {
          this.galleryActual--
        } else if ( this.galleryActual < this.allImages.length) {
          this.galleryActual++
        }
        this.choose(this.listImages[this.galleryActual].id)

      })
      .catch((e) => {
        alert(e)
        this.errors.push(e)
      })
    },

    getImages() {
      return axios
        .get(`images`)
        .then((listImage) => {
          // JSON responses are automatically parsed.
          this.listImages = listImage.data
          this.gallery()

        })
        .catch((e) => {
          alert(e)
          this.errors.push(e);
        })
        ;

    },

    cancel(id) {
      if(this.galleryActual == this.imageGettingChanged) {
        let formDataimage = new FormData();
        let blobdataimage = this.dataURItoBlob(this.imagePrec[id])

        let formDatavignette = new FormData();
        let blobdatavignette = this.dataURItoBlob(this.vignettePrec[id])

        this.imagePrec.pop()
        this.vignettePrec.pop()

        if(id == 0) {
          this.imagePrec = []
          this.vignettePrec = []
        }
        formDataimage.append('image', blobdataimage);
        formDatavignette.append('vignettedata', blobdatavignette);

      
        let promise = new Promise((resolve, reject) => {
          try {
            axios.post( '/images/' + this.galleryActual,
              formDataimage,
            ).then(() => {
              resolve()
              console.log('SUCCESS!!');
            })
            .catch(function(){
              console.log('FAILURE!!');
            });
          } catch(e) {
            reject();
          }
        })

        promise.then(() => {
          axios.post( '/images/' + this.imageGettingChanged + "?vignette=true",
            formDatavignette,
          ).then(() => {
            console.log('SUCCESS!!');
          })
          .catch(function(){
            console.log('FAILURE!!');
          });
        })
        .catch(
          (error) =>{
            alert(error)
            this.errors.push(error)
        }).finally(() => {

          document.getElementById("galleryCenter").setAttribute("src", this.imagePrec[id]);

          document.getElementById("download").setAttribute("href", document.getElementById("galleryCenter").src);
          document.getElementById("download").setAttribute("download", this.listImages[this.galleryActual].name);
          this.gallery();
        })
    }

      

      
    },

    dataURItoBlob(dataURL, dataTYPE) {
              var binary = atob(dataURL.split(',')[1]),
                array = [];
            for (var i = 0; i < binary.length; i++) array.push(binary.charCodeAt(i));
            return new Blob([new Uint8Array(array)], {
                type: dataTYPE || 'image/jpeg'
            });
          },

    wrongTint() {
    
    var valueteinte =  document.getElementById('teinte').value
    if (valueteinte < 0 || valueteinte > 360) {
          document.getElementById('wrongtint').style.display="block";
          document.getElementById('button_algo').disabled = true
          return;
        } else {
          document.getElementById('wrongtint').style.display="none";
          document.getElementById('button_algo').disabled = false
        }
    
    },

    open_button() {
      document.getElementById('button_algo').disabled = false
    },

    algo(url) {
      document.getElementById('loader').style.display="block";
      document.getElementById('button_algo').disabled = true
      if (this.hasChangedImage ) {
        this.imagePrec = []
        this.vignettePrec = []
        this.hasChangedImage = false
      } 
      this.imageGettingChanged = this.galleryActual
      this.imagePrec.push(document.getElementById('galleryCenter').src)
      this.vignettePrec.push(this.allImages[this.galleryActual])


      var algoToCall = "?algorithm="+this.algos[this.currentAlgo].text;
      if(this.currentAlgo==0) {
        algoToCall = algoToCall+"&gain="+document.getElementById('gain').value
      }
      if(this.currentAlgo==3) {
        algoToCall = algoToCall+"&filter="+this.filtres[this.filterType].text+"&filtersize="+document.getElementById('filtersize').value
      }
      if(this.currentAlgo==2) {
        var valueteinte =  document.getElementById('teinte').value
        
        algoToCall = algoToCall+"&teinte="+valueteinte
      }
        

      let promise = new Promise((resolve, reject) => {
        try {
          axios.get('/images/'+url+algoToCall, {responseType:"blob"})
              .then((dldImage) =>  {
                  var reader = new window.FileReader();
                  reader.readAsDataURL(dldImage.data);
                  reader.onload = () => {
                    document.getElementById("galleryCenter").setAttribute("src", reader.result);
                    var img = document.createElement("img");
                    img.onload = () => {
                      
                      var canvas = document.createElement("canvas");
                      var ctx = canvas.getContext("2d");
                      ctx.drawImage(img, 0, 0);

                      var MAX_WIDTH = 110;
                      var MAX_HEIGHT = 50;
                      var width = img.width;
                      var height = img.height;
                      if (width > height) {
                        if (width > MAX_WIDTH) {
                          height *= MAX_WIDTH / width;
                          width = MAX_WIDTH;
                        }
                      } else {
                        if (height > MAX_HEIGHT) {
                          width *= MAX_HEIGHT / height;
                          height = MAX_HEIGHT;
                        }
                      }
                      canvas.width = width;
                      canvas.height = height;
                      ctx = canvas.getContext("2d");
                      ctx.drawImage(img, 0, 0, width, height);
                      
                      var dataurl = canvas.toDataURL('image/jpeg',1.0);

                      resolve(dataurl)
                    }
                  img.src = reader.result
                  }
                  this.getImages();
                })
                .catch((e) => {
                  document.getElementById('loader').style.display="none";
                  alert(e)
                  this.errors.push(e);
                });
        } catch(e) {
          reject();
        }
      })

      promise.then(
        (result) => {

          let formData = new FormData();
          let blobdata = this.dataURItoBlob(result)

          formData.append('vignettedata', blobdata);

          axios.post( '/images/' + this.galleryActual + "?vignette=true",
            formData
          ).then(() => {
            console.log('SUCCESS!!');
          })
          .catch(function(){
            console.log('FAILURE!!');
          });
        })
        .catch(
        (error) =>{
          document.getElementById('loader').style.display="none";
          alert(error)
          this.errors.push(error)
        })
        .finally(() => {
          document.getElementById('loader').style.display="none";
          document.getElementById('button_algo').disabled = false
          this.gallery()
        })
    },

    downloadSelectedImage(url) {
      axios.get('/images/' + url, {responseType:"blob"})
           .then(function (dldImage) {
              var reader = new window.FileReader();
              reader.readAsDataURL(dldImage.data);
              reader.onload = function() {
                document.getElementById("imagedld").setAttribute("src", reader.result);
              }
            })
            .catch((e) => {
              this.errors.push(e);
            });
    },
    

    
    handleFileUpload(){
      this.file = this.$refs.file.files[0];
    },

    submitFile(){
      let formData = new FormData();
      formData.append('file', this.file);
      axios.post( '/images',
        formData,
        {
          headers: {
              'Content-Type': 'multipart/form-data'
          }
        }
      ).then(() => {
        this.getImages()
        console.log('SUCCESS!!');
      })
      .catch(function(){
        console.log('FAILURE!!');
      });
    }
  },

 


  mounted() {
    this.getImages();
  },
};








</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->

<style scoped>



  h3 {
    margin: 40px 0 0;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    display: inline-block;
    margin: 0 10px;
  }
  a {
    color: #42b983;
  }
  img {
      max-width: 100%;
      height: auto;
      
  }

  .galery{
    position:relative;
    height: 55vh;
    margin-left: 20vw;
    margin-right: 20vw;
    margin-top: 1%;
    margin-bottom: 1%;
    
    background-color:#555555
  }


  #galleryCenter {
    
    position: static;
    max-height: 50vh;
    max-width:50%;
    width: auto;
    height: auto;
    margin-top : 2.5vh ;
    background-color:#f44336
  }


  .arrow {
    border: solid #555555;
    border-width: 0 3px 3px 0;
    padding: 10px;
  }
  .arrow:hover{
    border : solid #f44336;
    border-width: 0 3px 3px 0;
  }

  .right {
    position:absolute;
    top:45%;
    right:-10%;
    float:right;
    transform: rotate(-45deg);
    -webkit-transform: rotate(-45deg);
    
  }

  .left {
    position:absolute;
    top:45%;
    left:-10%;
    float:left;
    transform: rotate(135deg);
    -webkit-transform: rotate(135deg);
    
  }
  .test{
    margin-top:1vh;
    margin-bottom:1vh;
    height: 100%;
    width: auto;
    margin-left: 20vw;
    margin-right: 20vw;
    border: 10px black;
    background-color:#555555;
  }

  .vignettes:hover {
      transform:scale(1.2);
  }


  .vignettes{
    margin-left: 3px;
    margin-right: 3px;
    margin-top: 5%;
    margin-bottom: 5%;
    max-height: 50px;
    max-width:110px;
    height:50px;
    border:1px ;
  }




  /********* Superposition simple de texte sur une image *******/

  /* Conteneur principal */
  .overlay-image {
  position: absolute;
  margin-left: 5%;
  width: 15%;
  }

  /* Image originale */
  .overlay-image .image {
  display: block;
  width: 100%;
  height: auto;
  }

  /* Texte original */
  .overlay-image .text {
  color: #fff;
  font-size: 20px;
  line-height: 1.5em;
  text-shadow: 2px 2px 2px rgb(0, 0, 0);
  text-align: center;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  }

  /********* Hover image et texte *******/

  /* Overlay */
  .overlay-image .hover {
  position: absolute;
  top: 0;
  height: 100%;
  width: 100%;
  opacity: 0;
  transition: .5s ease;
  }

  /* Apparition overlay sur passage souris */
  .overlay-image:hover .hover {
  opacity: 1;
  }

  /********* Hover background et texte uniquement *******/

  .overlay-image .normal {
  transition: .5s ease;
  }
  .overlay-image:hover .normal {
  opacity: 0;
  }
  .overlay-image .hover {
  background-color: rgba(0,0,0,0.5);
  }





  /********* Superposition simple de texte sur une image *******/

  /* Conteneur principal */
  .overlay-image2 {
  position: relative;
  margin-left: 90%;
  
  }

  /* Image originale */
  .overlay-image2 .image {
  display: block;
  width: 100%;
  height: auto;
  }

  /* Texte original */
  .overlay-image2 .text {
  color: #fff;
  font-size: 20px;
  line-height: 1.5em;
  text-shadow: 2px 2px 2px rgb(0, 0, 0);
  text-align: center;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  }


  /********* Hover image et texte *******/

  /* Overlay */
  .overlay-image2 .hover {
  position: absolute;
  top: 0;
  height: 100%;
  width: 100%;
  opacity: 0;
  transition: .5s ease;
  }

  /* Apparition overlay sur passage souris */
  .overlay-image2:hover .hover {
  opacity: 1;
  }

  /********* Hover background et texte uniquement *******/

  .overlay-image2 .normal {
  transition: .5s ease;
  }
  .overlay-image2:hover .normal {
  opacity: 0;
  }
  .overlay-image2 .hover {
  background-color: rgba(0,0,0,0.5);
  }




  .button {
      background-color: #4CAF50; /* Green */
      border: none;
      color: white;
      padding: 4px 10px;
      text-align: center;
      text-decoration: none;
      display: inline-block;
      font-size: 16px;
      margin: 4px 2px;
      -webkit-transition-duration: 0.4s; /* Safari */
      transition-duration: 0.4s;
      cursor: pointer;
  }

  .button1 {
      background-color: #f44336; 
      color: rgb(255, 255, 255); 
      border: 2px solid #f44336;
  }

  .button1:hover {
      background-color: white;
      color: black;
  }


  .button2 {
      background-color: #555555;
      color: white;
      border: 2px solid #555555;
  }

  .button2:hover {
      background-color: white;
      color: black;
  }
  .hello{
    background-image: url('../assets/aaa.jpg');
    background-size: cover;
  }
  
  #loader {
    position: sticky;
    left: 50%;
    bottom: 37px;
    animation: redBox 5s infinite;
  }

  @keyframes redBox {
    0% {
      left: 10px;
    }
    50% {
      left: 400px;
    }
    100% {
      left: 10px;
    }
  }


</style>




    