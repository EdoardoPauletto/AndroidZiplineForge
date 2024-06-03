import SwiftUI
import presenters
import shared

struct ContentView: View {
	 @State private var label = "..."

    var body: some View {
        
        Text(label)
            .font(.system(size: 36))
            .multilineTextAlignment(.leading)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(12)
        //questo è a parte
        .onAppear {
            //let scope = ZiplineZiplineScope()
            let scope = ExposedKt.mainScope()
            let worldClockIos = PresentersWorldClockIos(scope: scope)
            worldClockIos.start { model in
                self.label = model.label
            }
        }
        
    }
}

struct QuizView: View {
    @State private var mazziere: PresentersTriviaService?
    @State private var risposta: String = ""
    @State private var domandaNum = 0
    @State private var rispEsatte = [false]
    @State private var punteggio = 0
    @State private var enableAlert = false
    @State private var dialog = ""

    var body: some View {
        VStack{
            if (mazziere != nil){
                let gioca = mazziere?.games()[0]
                
                Text((gioca?.question[domandaNum].text)!)
                TextField("Risposta", text: $risposta )
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                Button("Invia"){
                    totDomande(dimensione: gioca!.question.count)
                    let result = mazziere?.answer(gameId: gioca!.id, questionId: gioca!.question[domandaNum].id, answer: risposta)
                    dialog = result!.message
                    if (result!.correct) {
                        rispEsatte[domandaNum] = true
                        punteggio = 0
                        punteggio = rispEsatte.filter({$0 == true}).count
                    }
                    enableAlert = true
                }
                    .buttonStyle(.bordered)
                    .disabled(rispEsatte[domandaNum])
                Button("Prossima domanda ->"){
                    totDomande(dimensione: gioca!.question.count)
                    if (domandaNum < (gioca?.question.count)!-1){
                        domandaNum+=1
                    } else {
                        domandaNum = 0
                    }
                    risposta = ""
                    //disableButton = false
                }
                    .buttonStyle(.bordered)
                Text("Punteggio: "+punteggio.codingKey.stringValue+"/"+(gioca?.question.count.codingKey.stringValue)!)
            } else {
                Text("Caricamento...")
                    .font(.system(size: 36))
                    .multilineTextAlignment(.leading)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(12)
            }
        }
        .alert(dialog, isPresented: $enableAlert) {
            Button("OK", role: .cancel) { }
        }
        .onAppear{
            let scope = ExposedKt.mainScope()
            let worldClockIos = PresentersWorldClockIos(scope: scope)
            worldClockIos.startTrivia{ oggetto in
                self.mazziere = oggetto
            }
        }
    }
    func totDomande(dimensione: Int){
        if (rispEsatte.count == 1 && rispEsatte[0] == false) {
            rispEsatte = Array(repeating: false, count: dimensione)
        } else if(rispEsatte.count != dimensione) {
            var tmp = Array(repeating: false, count: dimensione)
            var numDomande = 0
            if (rispEsatte.count > dimensione){
                numDomande = dimensione-1
            } else {
                numDomande = rispEsatte.count-1
            }
            for i in 0...numDomande{
                tmp[i] = rispEsatte[i]
            }
            rispEsatte = tmp
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		QuizView()
	}
}
