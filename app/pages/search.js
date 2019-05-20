import React, {Component} from 'react'
import {Dimensions, StyleSheet, Text, View, NativeModules} from 'react-native'
import {SearchBar} from 'react-native-elements'
import QuestionListView from "../components/QuestionListView";

export default class search extends Component {

    constructor(props) {
        super(props)
        this.state = {
            searchWord: '',
            questions: []
        }
    }

    updateSearch = (searchWord) => {
        this.state.searchWord = searchWord;
        this.setState({searchWord});
    }

    search = () => {
        console.log('开始搜索' + this.state.searchWord)
        NativeModules.LeetCodeMudule.searhQuestion(this.state.searchWord, (data) => {
            var temp = JSON.parse(data)
            temp = temp.filter((element, index, array) => {
                return element.question !== undefined && element.originQuestion !== undefined
            })

            if(temp.length > 0){
                this.setState({
                    questions: temp
                })

                this.listView.setFootHeight(150)
                this.listView.setQuestionList(temp)
                this.listView.setpageIndex(0)
            } else {
                this.setState({
                    questions: []
                })
            }
        }, (msg) => {
            alert(msg)
        })
    }

    render() {

            return (
                <View style={styles.container}>
                    <View style={styles.header}>
                        <View>
                            <SearchBar
                                placeholder="请输入要搜索的内容"
                                onChangeText={this.updateSearch}
                                value={this.state.searchWord}
                                onSubmitEditing={this.search}
                            />
                        </View>
                    </View>
                    {this.state.questions.length !== 0? (<QuestionListView
                        {...this.props}
                        ref={listView => this.listView = listView}
                        footHeight={200}
                    >
                    </QuestionListView>) : (<View></View>)}
                </View>

            )
    }
}

const styles = StyleSheet.create({
    container: {
        width: Dimensions.get('window').width,
    },
    header: {
        width: Dimensions.get('window').width,
    },
    SearchBar: {}
})
