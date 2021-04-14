<template>
  <div class="hello">

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
      <label>File
        <input type="file" id="file" ref="file" v-on:change="handleFileUpload()" />
      </label>
        <button v-on:click="submitFile()">Submit</button>
    </div>
  </div>

  <button v-on:click="removeImage()">SUPPRIMER CETTE IMAGE NULLE</button>


  <div class= "galery">
    <p><i class="arrow right" v-on:click="right()" ></i>
    <img src="" alt="pas d'image chargée" id = "galleryCenter"/>
    <i class="arrow left" v-on:click="left()"></i></p>
  </div>


  <div class="test">
   <img class="vignettes" v-for="(image,index) in allImages" :value="index" :key="image" :src="image" :alt="pout" v-on:click="choose(index)" />
  </div>

  <div class="algorithms">
    <select v-model="currentAlgo" alt="Selectionner un algo">
      <option  v-for="(algo, index) in algos" :value ="index" :key="algo"> 
        {{ algo.name }}
      </option>
    </select>
    
    <select v-if="currentAlgo == 3" v-model="filterType" alt="Selectionner un filtre">
      <option  v-for="(filtre, index) in filtres" :value ="index" :key="filtre"> 
        {{ filtre.text }}
      </option>
    </select>

    <input v-else-if="currentAlgo == 0 " id="gain" placeholder="Gain">
    <input v-else-if="currentAlgo == 2 " id="teinte" placeholder="Teinte (comprise entre 0 et 360)" size = 30>

    <input v-if="currentAlgo == 3" id="filtersize" placeholder="Niveau de flou">



  </div>  

  <button v-on:click="algo(listImages[galleryActual].id)">Apply Algorithm</button>

  <a download="" href="" title="DownloadImage" id="download">
    <img alt="Download Image">
  </a>
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
      displayedImage:0
    };
  },


  methods: {
    left() {
      if (this.galleryActual > 0) {
        this.galleryActual--;
      }
      this.choose(this.galleryActual);
    },
    right() {
      if (this.galleryActual < this.allImages.length-1) {
        this.galleryActual++;
      }
      this.choose(this.galleryActual);
    },


    choose(index){
      this.galleryActual = index
      axios.get('images/' + index, {responseType: "blob"})
              .then((displayedImage) => {
                var reader = new window.FileReader();
                reader.readAsDataURL(displayedImage.data);
                reader.onload = () => {
                  document.getElementById("galleryCenter").setAttribute("src", reader.result);
                  document.getElementById("download").setAttribute("href", this.allImages[this.galleryActual]);
                  document.getElementById("download").setAttribute("download", this.listImages[this.galleryActual].name);
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
        //alert(promises)
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
        if (this.galleryActual > 0) {
          this.galleryActual--
        } else if ( this.galleryActual < this.allImages.length) {
          this.galleryActual++
        }
        document.getElementById("galleryCenter").setAttribute("src", this.allImages[this.galleryActual])

      })
      .catch((e) => {
        alert(e)
        this.errors.push(e)
      })
    },

    getImages() {
      //alert("getImage")
      return axios
        .get(`images`)
        .then((listImage) => {
          // JSON responses are automatically parsed.
          //alert(listImage.data[0])
          this.listImages = listImage.data
          this.gallery()

        })
        .catch((e) => {
          alert(e)
          this.errors.push(e);
        })
        ;

    },

    algo(url) {

      //alert("pouet")

      var algoToCall = "?algorithm="+this.algos[this.currentAlgo].text;
      if(this.currentAlgo==0) {
        algoToCall = algoToCall+"&gain="+document.getElementById('gain').value
      }
      if(this.currentAlgo==3) {
        algoToCall = algoToCall+"&filter="+this.filtres[this.filterType].text+"&filtersize="+document.getElementById('filtersize').value
      }
      if(this.currentAlgo==2) {
        algoToCall = algoToCall+"&teinte="+document.getElementById('teinte').value
      }
        //alert(algoToCall)


      axios.get('/images/'+url+algoToCall, {responseType:"blob"})
           .then((dldImage) =>  {
              //alert("yoa");
              var reader = new window.FileReader();
              reader.readAsDataURL(dldImage.data);
              reader.onload = function() {
                //alert(reader.result)
                document.getElementById("galleryCenter").setAttribute("src", reader.result);
              }
              this.getImages();
            })
            .catch((e) => {
              alert(e)
              this.errors.push(e);
            });
    },

    downloadSelectedImage(url) {
      axios.get('/images/' + url, {responseType:"blob"})
           .then(function (dldImage) {
              //alert();
              var reader = new window.FileReader();
              reader.readAsDataURL(dldImage.data);
              reader.onload = function() {
                //alert(reader.result)
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
  top:15px;
  width:50%;
  margin-left: auto;
  margin-right: auto;
  bottom: 20px;
}
#galleryCenter {
  max-height: 500px;
  height:auto;
  width:auto;
}

.arrow {
  border: solid black;
  border-width: 0 3px 3px 0;
  padding: 10px;
}
.arrow:hover{
  border : solid rgb(43, 180, 226);
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
  margin-top:45px;
  width: auto;
  margin-left: auto;
  margin-right: auto;
  background-color: blue;
}
.vignettes{
  
  max-height: 50px;
  max-width:5%;
  width:auto;
  height:auto;
  border:3px solid blue;
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
 width: 10%;
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


</style>




    