import {View} from "@tarojs/components";
import {AtButton} from "taro-ui";
import "./index.scss";
import Taro from '@tarojs/taro';
import GlobalFooter from '../../components/globalFooter';

export default () => {
  return (
    <View className='indexPage'>

      <View className='at-article__h1 title'>MBTI 性格测试</View>
      <View className='at-article__h3 subTitle'>
        简单做几个测试，就可以准确地描述出你是谁，以及你的性格特点
      </View>

      <AtButton type='primary' size='normal' className='enterButton' circle onClick={() => {
        // 跳转到测试页面
        Taro.navigateTo({
          url: '/pages/Questionnaire/index'
        })
      }}
      >
        开始测试
      </AtButton>
      <GlobalFooter/>
    </View>

  );
};
