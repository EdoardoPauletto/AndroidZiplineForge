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

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
