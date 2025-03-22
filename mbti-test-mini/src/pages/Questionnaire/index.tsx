import {View} from "@tarojs/components";
import "./index.scss";
import GlobalFooter from '../../components/globalFooter';
// eslint-disable-next-line import/first
import {AtButton, AtRadio} from 'taro-ui'
import questions from "../../data/questions.json";
// eslint-disable-next-line import/first
import {useEffect, useState} from "react";
// eslint-disable-next-line import/first
import Taro from "@tarojs/taro";

/**
 * 题目页面
 */
export default () => {

  //当前题目的序号
  const [currentId, setCurrent] = useState<number>(1);
  //当前题目
  const [currentQuestion, setQuestion] = useState(questions[0]);
  //当前题目的选项
  const questionsOption = currentQuestion.options.map(q => {
    return {label: `${q.key}.${q.value}`, value: q.key};
  });
  //当前题目选择的答案提交
  const [currentOption, setCurrentOption] = useState<string>();
  //保存提交的答案，列表
  const [answers] = useState<string[]>([]);


  //序号变化的时候，改变当前题目和当前答案
  useEffect(
    () => {
      setQuestion(questions[currentId - 1]);
      setCurrentOption(answers[currentId - 1]);
    }, [currentId]
  )


  return (
    <View className='QuestionnairePage'>
      {JSON.stringify(answers)}
      <View className='at-article__h1 title'>{currentId}.{currentQuestion.title}</View>

      <View className='options'>
        <AtRadio
          options={questionsOption}
          value={currentOption}
          onClick={(value) => {
            setCurrentOption(value);
            answers[currentId - 1] = value;

          }}
        />
      </View>

      {currentId > 1 && (
        <AtButton className='controlButton' circle onClick={
          () => {
            setCurrent(currentId - 1);
            setQuestion(questions[currentId - 1]);
          }
        }
        >
          上一题
        </AtButton>)
      }

      {currentId < questions.length && (
        <AtButton type='primary' className='controlButton' circle onClick={
          () => {
            setCurrent(currentId + 1);
            setQuestion(questions[currentId - 1]);
          }
        } disabled={!currentOption}
        >
          下一题
        </AtButton>)
      }

      {currentId == questions.length && (
        <AtButton type='primary' className='controlButton' circle onClick={
          () => {
            //存储答案
            Taro.setStorageSync('answers', answers)
            // 跳转到结果页面
            Taro.navigateTo({
              url: '/pages/result/index'
            })
          }
        }
        >
          查看结果
        </AtButton>)
      }
      <GlobalFooter />
    </View>

  );
};
