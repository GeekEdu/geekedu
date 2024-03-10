<template>
  <div class="chatHome">
    <div class="chatLeft">
      <div class="title">
        <h1>直播间</h1>
      </div>
      <div class="dplayer">
        <VideoPlayer />
      </div>
    </div> 
    <div class="chatRight">
      <!-- <router-view></router-view> -->
      <div v-if="showChatWindow">
        <ChatWindow
          :frinedInfo="chatWindowInfo"
          @personCardSort="personCardSort"
        ></ChatWindow>
      </div>
      <div class="showIcon" v-else>
        <span class="iconfont icon-snapchat"></span>
        <!-- <img src="@/assets/img/snapchat.png" alt="" /> -->
      </div>
    </div>
  </div>
</template>

<script>
import PersonCard from "@/components/PersonCard.vue";
import ChatWindow from "./chatwindow.vue";
import VideoPlayer from '@/components/VideoPlayer.vue'

import { getFriend } from "@/api/getData";
export default {
  name: "App",
  components: {
    PersonCard,
    ChatWindow,
    VideoPlayer,
  },
  data() {
    return {
      pcCurrent: "",
      personList: [], // 好友列表
      showChatWindow: true,
      chatWindowInfo: {},
      centerDialogVisible: false
    };
  },
  mounted() {
    getFriend().then((res) => {
      console.log(res);
      this.personList = res;
    });
  },
  methods: {
    clickPerson(info) {
      this.showChatWindow = true;
      this.chatWindowInfo = info;
      this.personInfo = info;
      this.pcCurrent = info.id;
    },
    personCardSort(id) {
      if (id !== this.personList[0].id) {
        console.log(id);
        let nowPersonInfo;
        for (let i = 0; i < this.personList.length; i++) {
          if (this.personList[i].id == id) {
            nowPersonInfo = this.personList[i];
            this.personList.splice(i, 1);
            break;
          }
        }
        this.personList.unshift(nowPersonInfo);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.chatHome {
  // margin-top: 20px;
  height: 100%;
  width: 100%;
  display: flex;
  .chatLeft {
    width: 900px;
    .title {
      color: #fff;
      padding-left: 10px;
    }
    .dplayer {
      width: 100%;
      height: 500px;
    }
  }

  .chatRight {
    flex: 1;
    width: 400px;
    // padding-right: 30px;
    .showIcon {
      position: absolute;
      top: calc(50% - 150px); /*垂直居中 */
      left: calc(50% - 50px); /*水平居中 */
      .icon-snapchat {
        width: 300px;
        height: 300px;
        font-size: 300px;
        // color: rgb(28, 30, 44);
      }
    }
  }
}
</style>
