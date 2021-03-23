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
        <button v-on:click="submitFile(),getImages()">Submit</button>
    </div>
  </div>
  <div class= "galery">
    <button v-on:click="gallery()">Afficher/Mettre à jour la gallerie d'Images</button>
  </div>


  <img v-for="image in allImages" :key="image" :src="image" :alt="pou" />

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
      pouet:1,
      file: '',
      dldImage: "blob",
      galeryImage: "blob",
      allImages:[],
      errors: [],
    };
  },


  methods: {
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
          //alert(this.allImages[0])
          //alert(this.allImages[1])
          //alert(this.allImages[2])
        }
      ).catch(
        (error) =>{
          alert(error)
          this.errors.push(error)
        }
      ).finally(
        () => alert("pouet")
      )
    },

    getImages() {
      //alert("getImage")
      return axios
        .get(`images`)
        .then((listImage) => {
          // JSON responses are automatically parsed.
          this.listImages = listImage.data
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
      ).then(function(){
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
</style>




    