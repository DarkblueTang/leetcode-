import React, {Component} from "react";
import {createBottomTabNavigator} from 'react-navigation'
import {Image, StyleSheet} from 'react-native'
import home from '../pages/home'
import tags from '../pages/tags'
import search from '../pages/search'
import me from '../pages/me'

var tabrouter = createBottomTabNavigator({
    home: {
        screen: home,
        navigationOptions: {
            header: null,
            tabBarLabel: '主页',
            tabBarIcon: ({tintColor, focused}) => {
                if (focused) {
                    return (<Image style={sytles.icon} source={require('../image/homeF.png')}></Image>)
                } else {
                    return (<Image style={sytles.icon} source={require('../image/home.png')}></Image>)
                }
            },
        }
    },
    tags: {
        screen: tags,
        navigationOptions: {
            header: null,
            tabBarLabel: '标签',
            tabBarIcon: ({tintColor, focused}) => {
                if (focused) {
                    return (<Image style={sytles.icon} source={require('../image/TagF.png')}></Image>)
                } else {
                    return (<Image style={sytles.icon} source={require('../image/Tag.png')}></Image>)
                }
            },
        }
    },
    search: {
        screen: search,
        navigationOptions: {
            header: null,
            tabBarLabel: '搜索',
            tabBarIcon: ({tintColor, focused}) => {
                if (focused) {
                    return (<Image style={sytles.icon} source={require('../image/searchF.png')}></Image>)
                } else {
                    return (<Image style={sytles.icon} source={require('../image/search.png')}></Image>)
                }
            },
        }
    },
    me: {
        screen: me,
        navigationOptions: {
            header: null,
            tabBarLabel: '我',
            tabBarIcon: ({tintColor, focused}) => {
                if (focused) {
                    return (<Image style={sytles.icon} source={require('../image/meF.png')}></Image>)
                } else {
                    return (<Image style={sytles.icon} source={require('../image/me.png')}></Image>)
                }
            },
        }
    }
}, {
    initialRouterName: 'home',
    tabBarOptions: {
        activeTintColor: '#6a74a0',
        labelStyle: {
            fontSize: 11
        }
    },
    animationEnabled: true,
})

const sytles = StyleSheet.create({
    icon: {
        width: 25,
        height: 25
    }
})

export default tabrouter
