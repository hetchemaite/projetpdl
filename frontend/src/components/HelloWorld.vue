<template>
  <div class="hello">

    <h3>{{ listImages }}</h3>

    <select @change="downloadSelectedImage(selected)" v-model="selected" >
      <option  v-for="(image, index) in listImages" :value ="index" :key="image"> 
        {{ image }}
      </option>
    </select>


<span> {{dldImage.data}} </span>

<img src alt="" id="imagedld">

  <div class="container">
    <div class="large-12 medium-12 small-12 cell">
      <label>File
        <input type="file" id="file" ref="file" v-on:change="handleFileUpload()" />
      </label>
        <button v-on:click="submitFile()">Submit</button>
    </div>
  </div>

  <div class= "galery">
    <p><i class="arrow right" v-on:click="right()" ></i>
    <img src="http://localhost:8089/images/0" alt="pas d'image chargée" id = "galleryCenter" />
    <i class="arrow left" v-on:click="left()"></i></p>
  </div>


  <div class="test">
   <img class="vignettes" v-for="image in allImages" :key="image" :src="image" :alt="pout" />
  </div>
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
      
    };
  },


  methods: {

    left() {
      if (this.galleryActual > 0) {
        this.galleryActual--;
      }
      alert(this.galleryActual)
      document.getElementById("galleryCenter").setAttribute("src", this.allImages[this.galleryActual]);
    },
    right() {
      if (this.galleryActual < this.allImages.length-1) {
        this.galleryActual++;
      }
      alert(this.galleryActual)
      document.getElementById("galleryCenter").setAttribute("src", this.allImages[this.galleryActual]);
    },
    gallery() {
      this.allImages=[];
      let promises = [];

      function asyncGallery (i) {
        return new Promise((resolve, reject) => {
          try {
            axios.get('images/' + i, {responseType: "blob"})
              .then((galeryImage) => {
                var reader = new window.FileReader();
                reader.readAsDataURL(galeryImage.data);
                reader.onload = () => {
                  resolve(reader.result)
                }
              })
              .catch((e) => {
                this.errors.push(e);
              })

          } catch(e) {
            reject()
          }
        })
      }

    for(let i = 0 ; i < this.listImages.length ; i++) {
        promises.push(asyncGallery(i))
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
      ).finally(
        //alert("yo")
      )
    },

    getImages() {
      //alert("getImage")
      return axios
        .get(`images`)
        .then((listImage) => {
          // JSON responses are automatically parsed.
          this.listImages = listImage.data
          this.gallery()
        })
        .catch((e) => {
          this.errors.push(e);
        });
      
    },

    downloadSelectedImage(url) {
      axios.get('http://localhost:8081/images/' + url, {responseType:"blob"})
           .then(function (dldImage) {
              //alert();
              var reader = new window.FileReader();
              reader.readAsDataURL(dldImage.data);
              reader.onload = function() {
                alert(reader.result)
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
  background-color: black;
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
  top:50%;
  right:-10%;
  float:right;
  transform: rotate(-45deg);
  -webkit-transform: rotate(-45deg);
}

.left {
  position:absolute;
  top:50%;
  left:-10%;
  float:left;
  transform: rotate(135deg);
  -webkit-transform: rotate(135deg);
}
.test{
  width: auto;
  margin-left: auto;
  margin-right: auto;
  background-color: blue;
}
.vignettes{
  max-height: 5%;
  max-width:5%;
  width:auto;
  height:auto;
  border:3px solid blue;
}


</style>




    