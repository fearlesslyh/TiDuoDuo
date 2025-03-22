import {View} from "@tarojs/components";
import "./index.scss";
import GlobalFooter from '../../components/globalFooter';
// eslint-disable-next-line import/first
import Taro from '@tarojs/taro';
// eslint-disable-next-line import/first
import {AtButton} from 'taro-ui';
import questionsResults from "../../data/questionsResults.json";
import questions from "../../data/questions.json";
import {getBestQuestionResult} from "../../utils/assetUtil";

export default () => {
  //获取答案
  const answers = Taro.getStorageSync('answers');
  {JSON.stringify(answers)}
  if (!answers|| answers.length <1) {
    Taro.showToast({
      title: '失败',
      icon: 'error',
      duration: 3000
    });
  }
  //获取结果
  const result= getBestQuestionResult(answers, questions, questionsResults);

  return (
    <View className='resultPage'>
      <View className='at-article__h1 title'>{result.resultName}</View>
      <View className='at-article__h3 subTitle'>
        {result.resultDesc}
      </View>
      <AtButton type='primary' size='normal' className='enterButton' circle onClick={() => {
        //跳转到主页
        Taro.reLaunch({
          url: '/pages/index/index'
        })
      }}
      >
        返回主页
      </AtButton>
      <GlobalFooter />
    </View>
  );
};
