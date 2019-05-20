import React, {Component} from 'react'
import {NativeModules, StyleSheet, Text, View} from 'react-native'
import QuestionListView from '../components/QuestionListView'

export default class home extends Component {

    constructor(props) {
        super(props)
        this.state = {
            questionList: [],
        }
        this.getAllQuestion()
    }

    getAllQuestion = () => {
        NativeModules.LeetCodeMudule.getAllQuestions((data) => {
            var temp = JSON.parse(data)
            temp = temp.filter((element, index, array) => {
                return element.question !== undefined && element.originQuestion !== undefined
            })

            this.listView.setQuestionList(temp)
            this.setState({
                questionList: temp
            })
        }, (msg) => {
            alert(msg)
        })
    }

    render() {
        return (
            <View style={styles.container}>
                <QuestionListView
                    {...this.props}
                    ref={listView => this.listView = listView}
                >
                </QuestionListView>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        alignItems: 'center',
    }
})
