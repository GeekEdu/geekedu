<template lang="">
  <div id="dplayer" style="width:900px; height:500px">
    
  </div>
</template>
<script>
import DPlayer from 'dplayer'
export default {
  name: "VideoPlayer",

  mounted() {
    this.initPlayer()
  },

  methods: {
    initPlayer() {
      const player = new DPlayer({
        container: document.getElementById('dplayer'),
        video: {
          url: 'https://meedu.cloud.oss.meedu.vip/video/meedu%E4%BB%8B%E7%BB%8D-2021-09-09.mp4',
          type: 'auto'
        },
        theme: '#FADFA3',
      })
    }
  }
}
</script>
<style lang="">
  
</style>
