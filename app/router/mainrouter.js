import React from "react";
import {createStackNavigator, createAppContainer} from 'react-navigation'
import StackViewStyleInterpolator from "react-navigation-stack/src/views/StackView/StackViewStyleInterpolator";

import login from '../pages/login'
import questionDetail from '../pages/questionDetail'
import tabrouter from './tabrouter'
import tagQuestionList from '../pages/tagQuestionList'

const AppNavigator = createStackNavigator({

    login: {
        screen: login,
        // screen: tabrouter,
        navigationOptions: {
            header: null
        }
    },
    main: {
        screen: tabrouter,
        navigationOptions: {
            header: null
        }
    },
    questionDetail: {
        screen: questionDetail,
        navigationOptions: {
            title: '题目详情'
        }
    },
    tagQuestionList: {
        screen: tagQuestionList,
        navigationOptions: ({navigation}) => {
            const {params} = navigation.state

            return {
                title: params.title ? params.title : '标签',
            }
        }
    }
}, {
    initialRouterName: 'main',
    mode: 'card',
    cardShadowEnabled: true,
    transitionConfig: () => ({
        screenInterpolator: StackViewStyleInterpolator.forHorizontal
    })
});

export default createAppContainer(AppNavigator);
