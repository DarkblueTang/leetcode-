import React, {Component} from 'react'
import {Text, View, NativeModules} from 'react-native'
import QuestionListView from "../components/QuestionListView";

export default class tagQuestionList extends Component {

    constructor(props) {
        super(props)

        this.state = {
            tag: null
        }

        this.props.navigation.setParams({title: '唐泽宇'})
        this.setTitle()
        this.getQuestion()
    }

    setTitle = () => {
        this.state.tag = this.props.navigation.getParam("tag")
        this.props.navigation.setParams({title: this.state.tag.titleZN})
    }

    getQuestion = () => {
        NativeModules.LeetCodeMudule.getQuestionByTag(this.state.tag.searchTitle, (data) => {
            this.listView.setQuestionList(JSON.parse(data))

        }, (msg) => {
            alert(msg)
        })
    }

    render() {
        return (
            <View>
                <QuestionListView
                    {...this.props}
                    ref={listView => this.listView = listView}
                ></QuestionListView>
            </View>
        )
    }
}
